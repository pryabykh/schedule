package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;

public class ClassroomDtoUtils {
    public static ClassroomResponseDto convertFromEntity(Classroom classroom) {
        ClassroomResponseDto classroomDto = new ClassroomResponseDto();
        classroomDto.setId(classroom.getId());
        classroomDto.setNumber(classroom.getNumber());
        classroomDto.setCapacity(classroom.getCapacity());
        classroomDto.setDescription(classroom.getDescription());
        classroomDto.setCreatorId(classroom.getCreatorId());
        classroomDto.setTeacher(TeacherDtoUtils.convertFromEntityWithoutClassroom(classroom.getTeacher()));
        classroomDto.setVersion(classroom.getVersion());
        classroomDto.setCreatedAt(classroom.getCreatedAt());
        classroomDto.setUpdatedAt(classroom.getUpdatedAt());
        return classroomDto;
    }

    public static ClassroomResponseDto convertFromEntityWithoutTeacher(Classroom classroom) {
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

    public static Classroom convertToEntity(ClassroomRequestDto classroomDto) {
        Classroom classroom = new Classroom();
        classroom.setNumber(classroomDto.getNumber());
        classroom.setCapacity(classroomDto.getCapacity());
        classroom.setDescription(classroomDto.getDescription());
        if (classroomDto.getTeacher() != null) {
            Teacher teacherInCharge = new Teacher();
            teacherInCharge.setId(classroomDto.getTeacher());
            classroom.setTeacher(teacherInCharge);
        }
        return classroom;
    }
}
