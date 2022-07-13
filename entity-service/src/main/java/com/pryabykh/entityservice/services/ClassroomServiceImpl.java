package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.ClassroomAlreadyExistsException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
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
            throw new ClassroomAlreadyExistsException();
        }
        Classroom savedClassroom = classroomRepository.save(ClassroomDtoUtils.convertToEntity(classroomDto));
        return ClassroomDtoUtils.convertFromEntity(savedClassroom);
    }

    private boolean classroomWithGivenNumberAlreadyExists(ClassroomRequestDto classroomDto) {
        return classroomRepository.findByNumber(classroomDto.getNumber()).isPresent();
    }
}
