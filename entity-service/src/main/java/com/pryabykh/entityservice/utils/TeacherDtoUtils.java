package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.userContext.UserContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

public class TeacherDtoUtils {
    public static TeacherResponseDto convertFromEntity(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        teacherResponseDto.setId(teacher.getId());
        teacherResponseDto.setFirstName(teacher.getFirstName());
        teacherResponseDto.setPatronymic(teacher.getPatronymic());
        teacherResponseDto.setLastName(teacher.getLastName());
        teacherResponseDto.setCreatorId(teacher.getCreatorId());
        teacherResponseDto.setVersion(teacher.getVersion());
        teacherResponseDto.setCreatedAt(teacher.getCreatedAt());
        teacherResponseDto.setUpdatedAt(teacher.getUpdatedAt());
        Set<ClassroomResponseDto> classroomsResponse = teacher.getClassrooms()
                .stream()
                .map((ClassroomDtoUtils::convertFromEntityNoRecursion))
                .collect(Collectors.toSet());
        teacherResponseDto.setClassrooms(classroomsResponse);
        return teacherResponseDto;
    }

    public static TeacherResponseDto convertFromEntityNoRecursion(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        teacherResponseDto.setId(teacher.getId());
        teacherResponseDto.setFirstName(teacher.getFirstName());
        teacherResponseDto.setPatronymic(teacher.getPatronymic());
        teacherResponseDto.setLastName(teacher.getLastName());
        teacherResponseDto.setCreatorId(teacher.getCreatorId());
        teacherResponseDto.setVersion(teacher.getVersion());
        teacherResponseDto.setCreatedAt(teacher.getCreatedAt());
        teacherResponseDto.setUpdatedAt(teacher.getUpdatedAt());
        return teacherResponseDto;
    }

    public static Teacher convertToEntity(TeacherRequestDto teacherRequestDto,
                                          ClassroomRepository classroomRepository) {
        if (teacherRequestDto == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacherRequestDto.getFirstName());
        teacher.setPatronymic(teacherRequestDto.getPatronymic());
        teacher.setLastName(teacherRequestDto.getLastName());
        Long userId = UserContextHolder.getContext().getUserId();
        Set<Classroom> classrooms = teacherRequestDto.getClassrooms()
                .stream()
                .map((classroomId) -> {
                    return classroomRepository.findByIdAndCreatorId(classroomId, userId).orElseThrow(EntityNotFoundException::new);
                })
                .collect(Collectors.toSet());
        teacher.setClassrooms(classrooms);
        return teacher;
    }
}
