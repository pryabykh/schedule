package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.EntityAlreadyExistsException;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.ClassroomDtoUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    @Transactional
    public ClassroomResponseDto create(ClassroomRequestDto classroomDto) {
        Long userId = UserContextHolder.getContext().getUserId();
        if (classroomWithGivenNumberAlreadyExists(classroomDto, userId)) {
            throw new EntityAlreadyExistsException();
        }
        Classroom classroomEntity = ClassroomDtoUtils.convertToEntity(classroomDto);
        classroomEntity.setCreatorId(userId);
        Classroom savedClassroom = classroomRepository.save(classroomEntity);
        return ClassroomDtoUtils.convertFromEntity(savedClassroom);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassroomResponseDto> fetchAll(PageSizeDto pageSizeDto) {
        Pageable pageable = createPageable(pageSizeDto);
        Long userId = UserContextHolder.getContext().getUserId();
        if (!hasFiltration(pageSizeDto)) {
            return classroomRepository.findAllByCreatorId(userId, pageable)
                    .map(ClassroomDtoUtils::convertFromEntity);
        }
        return fetchAllByFilterAndCreatorId(pageSizeDto.getFilterBy(),
                pageSizeDto.getFilterValue(),
                userId,
                pageable)
                .map(ClassroomDtoUtils::convertFromEntity);
    }

    @Override
    public ClassroomResponseDto fetchById(Long id) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(id);
        if (optionalClassroom.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Long creatorId = optionalClassroom.get().getCreatorId();
        Long currentUserId = UserContextHolder.getContext().getUserId();
        if (!creatorId.equals(currentUserId)) {
            throw new PermissionDeniedException();
        }
        return ClassroomDtoUtils.convertFromEntity(optionalClassroom.get());
    }

    @Override
    public ClassroomResponseDto update(Long id, ClassroomRequestDto classroomDto) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(id);
        if (optionalClassroom.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Classroom currentClassroom = optionalClassroom.get();
        Long userId = UserContextHolder.getContext().getUserId();
        if (!Objects.equals(currentClassroom.getCreatorId(), userId)) {
            throw new PermissionDeniedException();
        }
        if (!Objects.equals(currentClassroom.getNumber(), classroomDto.getNumber())) {
            Optional<Classroom> classroomByNumberAndCreatorId =
                    classroomRepository.findByNumberAndCreatorId(classroomDto.getNumber(), userId);
            if (classroomByNumberAndCreatorId.isPresent()) {
                throw new EntityAlreadyExistsException();
            }
        }
        Classroom newClassroom = ClassroomDtoUtils.convertToEntity(classroomDto);
        currentClassroom.setNumber(newClassroom.getNumber());
        currentClassroom.setCapacity(newClassroom.getCapacity());
        currentClassroom.setDescription(newClassroom.getDescription());
        return ClassroomDtoUtils.convertFromEntity(classroomRepository.save(currentClassroom));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(id);
        if (optionalClassroom.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Long creatorId = optionalClassroom.get().getCreatorId();
        Long currentUserId = UserContextHolder.getContext().getUserId();
        if (!creatorId.equals(currentUserId)) {
            throw new PermissionDeniedException();
        }
        classroomRepository.deleteById(id);
    }

    private boolean classroomWithGivenNumberAlreadyExists(ClassroomRequestDto classroomDto, Long userId) {
        return classroomRepository.findByNumberAndCreatorId(classroomDto.getNumber(), userId).isPresent();
    }

    private Pageable createPageable(PageSizeDto pageSizeDto) {
        int page = pageSizeDto.getPage();
        int size = pageSizeDto.getSize();
        String sortBy = pageSizeDto.getSortBy();
        String sortDirection = pageSizeDto.getSortDirection();
        if (sortBy == null || sortDirection == null) {
            return PageRequest.of(page, size);
        }
        if (sortDirection.equalsIgnoreCase("ASC")) {
            return PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else if (sortDirection.equalsIgnoreCase("DESC")) {
            return PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else {
            throw new IllegalArgumentException("sortDirection can be ASC or DESC only");
        }
    }

    private boolean hasFiltration(PageSizeDto pageSizeDto) {
        String filterBy = pageSizeDto.getFilterBy();
        String filterValue = pageSizeDto.getFilterValue();
        if (filterBy == null || filterBy.equals("") || filterValue == null  || filterValue.equals("")) {
            return false;
        }
        return true;
    }

    private Page<Classroom> fetchAllByFilterAndCreatorId(String filterBy,
                                                         String filterValue,
                                                         Long creatorId,
                                                         Pageable pageable) {
        switch (filterBy.toLowerCase()) {
            case "number": {
                return classroomRepository.findByCreatorIdAndNumberContainingIgnoreCase(creatorId, filterValue, pageable);
            }
            case "capacity": {
                return classroomRepository.findByCreatorIdAndCapacityContaining(creatorId, filterValue, pageable);
            }
            case "description": {
                return classroomRepository.findByCreatorIdAndDescriptionContainingIgnoreCase(creatorId, filterValue, pageable);
            }
            case "teacher": {
                return classroomRepository.findByCreatorIdAndTeacherIdContaining(creatorId, filterValue, pageable);
            }
            default: {
                throw new IllegalArgumentException("Unsupported filter criteria - " + filterBy);
            }
        }
    }
}
