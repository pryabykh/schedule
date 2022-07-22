package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;

import java.util.Collections;
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
                .map((ClassroomDtoUtils::convertFromEntityWithoutTeacher))
                .collect(Collectors.toSet());
        teacherResponseDto.setClassrooms(classroomsResponse);
        return teacherResponseDto;
    }

    public static TeacherResponseDto convertFromEntityWithoutClassroom(Teacher teacher) {
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

    public static Teacher convertToEntity(TeacherRequestDto teacherRequestDto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacherRequestDto.getFirstName());
        teacher.setPatronymic(teacherRequestDto.getPatronymic());
        teacher.setLastName(teacherRequestDto.getLastName());
        teacherRequestDto.getClassrooms().forEach((classroomId) -> {
            Classroom classroom = new Classroom();
            classroom.setId(classroomId);
            teacher.getClassrooms().add(classroom);
        });
        return teacher;
    }
}
