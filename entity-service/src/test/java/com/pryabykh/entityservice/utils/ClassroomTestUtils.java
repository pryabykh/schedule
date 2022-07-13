package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.models.Classroom;

import java.util.Date;

public class ClassroomTestUtils {
    public static ClassroomRequestDto shapeClassroomRequestDto() {
        ClassroomRequestDto classroomDto = new ClassroomRequestDto();
        classroomDto.setNumber("1-B");
        classroomDto.setCapacity(20);
        classroomDto.setDescription("Кабинет химии");
        return classroomDto;
    }

    public static ClassroomRequestDto shapeInvalidClassroomRequestDto() {
        ClassroomRequestDto classroomDto = new ClassroomRequestDto();
        classroomDto.setNumber(null);
        classroomDto.setCapacity(0);
        classroomDto.setDescription("Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химииКабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химииКабинет химии Кабинет химии Кабинет химии Кабинет химии");
        return classroomDto;
    }

    public static Classroom shapeClassroomEntity() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setNumber("1-B");
        classroom.setCapacity(20);
        classroom.setDescription("Кабинет химии");
        classroom.setCreatorId(1L);
        classroom.setVersion(1);
        classroom.setCreatedAt(new Date());
        classroom.setUpdatedAt(new Date());
        return classroom;
    }

    public static ClassroomResponseDto shapeClassroomResponseDto() {
        ClassroomResponseDto classroomDto = new ClassroomResponseDto();
        classroomDto.setId(1L);
        classroomDto.setNumber("1-B");
        classroomDto.setCapacity(20);
        classroomDto.setDescription("Кабинет химии");
        classroomDto.setCreatorId(1L);
        classroomDto.setVersion(1);
        classroomDto.setCreatedAt(new Date());
        classroomDto.setUpdatedAt(new Date());
        return classroomDto;
    }
}
