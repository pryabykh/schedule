package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.repositories.TeacherRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.TeacherDtoUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, ClassroomRepository classroomRepository) {
        this.teacherRepository = teacherRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    @Transactional
    public TeacherResponseDto create(TeacherRequestDto teacherDto) {
        Long userId = UserContextHolder.getContext().getUserId();
        Teacher teacherEntity = TeacherDtoUtils.convertToEntity(teacherDto);
        teacherEntity.setCreatorId(userId);
        Teacher savedTeacher = teacherRepository.save(teacherEntity);
        teacherDto.getClassrooms().forEach((classroomId) -> {
            Optional<Classroom> optionalClassroom = classroomRepository.findByIdAndCreatorId(classroomId, userId);
            if (optionalClassroom.isPresent()) {
                Teacher teacher = new Teacher();
                teacher.setId(savedTeacher.getId());
                Classroom classroom = optionalClassroom.get();
                classroom.setTeacher(teacher);
                classroomRepository.save(classroom);
            }
        });
        return TeacherDtoUtils.convertFromEntity(savedTeacher);
    }

    @Override
    public Page<TeacherResponseDto> fetchAll(PageSizeDto pageSizeDto) {
        return null;
    }

    @Override
    public TeacherResponseDto fetchById(Long id) {
        return null;
    }

    @Override
    public TeacherResponseDto update(Long id, TeacherRequestDto teacherDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
