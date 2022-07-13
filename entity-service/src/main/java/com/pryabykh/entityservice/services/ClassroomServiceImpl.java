package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.EntityAlreadyExistsException;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.ClassroomDtoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (classroomWithGivenNumberAlreadyExists(classroomDto)) {
            throw new EntityAlreadyExistsException();
        }
        Classroom classroomEntity = ClassroomDtoUtils.convertToEntity(classroomDto);
        classroomEntity.setCreatorId(UserContextHolder.getContext().getUserId());
        Classroom savedClassroom = classroomRepository.save(classroomEntity);
        return ClassroomDtoUtils.convertFromEntity(savedClassroom);
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

    private boolean classroomWithGivenNumberAlreadyExists(ClassroomRequestDto classroomDto) {
        return classroomRepository.findByNumber(classroomDto.getNumber()).isPresent();
    }
}
