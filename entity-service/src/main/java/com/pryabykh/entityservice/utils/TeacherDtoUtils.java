package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.models.Teacher;

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
        return teacherResponseDto;
    }
}
