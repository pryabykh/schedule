package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.EntityAlreadyExistsException;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.repositories.SubjectRepository;
import com.pryabykh.entityservice.repositories.TeacherRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.ClassroomDtoUtils;
import com.pryabykh.entityservice.utils.CommonUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, SubjectRepository subjectRepository, TeacherRepository teacherRepository) {
        this.classroomRepository = classroomRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    public ClassroomResponseDto create(ClassroomRequestDto classroomDto) {
        Long userId = UserContextHolder.getContext().getUserId();
        if (classroomWithGivenNumberAlreadyExists(classroomDto, userId)) {
            throw new EntityAlreadyExistsException();
        }
        Classroom classroomEntity = ClassroomDtoUtils.convertToEntity(classroomDto,
                subjectRepository,
                teacherRepository);
        classroomEntity.setCreatorId(userId);
        Classroom savedClassroom = classroomRepository.save(classroomEntity);
        return ClassroomDtoUtils.convertFromEntity(savedClassroom);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassroomResponseDto> fetchAll(PageSizeDto pageSizeDto) {
        Pageable pageable = CommonUtils.createPageable(pageSizeDto);
        Long userId = UserContextHolder.getContext().getUserId();
        if (!CommonUtils.hasFiltration(pageSizeDto)) {
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
        Classroom newClassroom = ClassroomDtoUtils.convertToEntity(classroomDto,
                subjectRepository,
                teacherRepository);
        currentClassroom.setNumber(newClassroom.getNumber());
        currentClassroom.setCapacity(newClassroom.getCapacity());
        currentClassroom.setDescription(newClassroom.getDescription());
        currentClassroom.setSubjects(newClassroom.getSubjects());
        currentClassroom.setTeacher(newClassroom.getTeacher());
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
            case "subjects": {
                return classroomRepository.findByCreatorIdAndSubjectsContaining(creatorId, filterValue, pageable);
            }
            default: {
                throw new IllegalArgumentException("Unsupported filter criteria - " + filterBy);
            }
        }
    }
}
