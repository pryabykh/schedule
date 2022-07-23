package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Subject;
import com.pryabykh.entityservice.models.Teacher;
import com.pryabykh.entityservice.repositories.SubjectRepository;
import com.pryabykh.entityservice.repositories.TeacherRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

public class ClassroomDtoUtils {
    public static ClassroomResponseDto convertFromEntity(Classroom classroom) {
        if (classroom == null) {
            return null;
        }
        ClassroomResponseDto classroomDto = new ClassroomResponseDto();
        classroomDto.setId(classroom.getId());
        classroomDto.setNumber(classroom.getNumber());
        classroomDto.setCapacity(classroom.getCapacity());
        classroomDto.setDescription(classroom.getDescription());
        classroomDto.setCreatorId(classroom.getCreatorId());
        classroomDto.setTeacher(TeacherDtoUtils.convertFromEntityNoRecursion(classroom.getTeacher()));
        Set<SubjectResponseDto> subjectResponseDtos = classroom.getSubjects()
                .stream()
                .map((SubjectDtoUtils::convertFromEntityNoRecursion))
                .collect(Collectors.toSet());
        classroomDto.setSubjects(subjectResponseDtos);
        classroomDto.setVersion(classroom.getVersion());
        classroomDto.setCreatedAt(classroom.getCreatedAt());
        classroomDto.setUpdatedAt(classroom.getUpdatedAt());
        return classroomDto;
    }

    public static ClassroomResponseDto convertFromEntityNoRecursion(Classroom classroom) {
        if (classroom == null) {
            return null;
        }
        ClassroomResponseDto classroomDto = new ClassroomResponseDto();
        classroomDto.setId(classroom.getId());
        classroomDto.setNumber(classroom.getNumber());
        classroomDto.setCapacity(classroom.getCapacity());
        classroomDto.setDescription(classroom.getDescription());
        classroomDto.setCreatorId(classroom.getCreatorId());
        classroomDto.setVersion(classroom.getVersion());
        classroomDto.setCreatedAt(classroom.getCreatedAt());
        classroomDto.setUpdatedAt(classroom.getUpdatedAt());
        return classroomDto;
    }

    public static Classroom convertToEntity(ClassroomRequestDto classroomDto,
                                            SubjectRepository subjectRepository,
                                            TeacherRepository teacherRepository) {
        if (classroomDto == null) {
            return null;
        }
        Classroom classroom = new Classroom();
        classroom.setNumber(classroomDto.getNumber());
        classroom.setCapacity(classroomDto.getCapacity());
        classroom.setDescription(classroomDto.getDescription());
        if (classroomDto.getTeacher() != null) {
            Long userId = UserContextHolder.getContext().getUserId();
            Teacher teacher = teacherRepository.findByIdAndCreatorId(classroomDto.getTeacher(), userId)
                    .orElseThrow(EntityNotFoundException::new);
            classroom.setTeacher(teacher);
        }
        Set<Subject> subjects = classroomDto.getSubjects()
                .stream()
                .map((subjectId) -> {
                    return subjectRepository.findById(subjectId).orElseThrow(EntityNotFoundException::new);
                })
                .collect(Collectors.toSet());
        classroom.setSubjects(subjects);
        return classroom;
    }
}
