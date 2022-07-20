package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassroomTestUtils {
    public static ClassroomRequestDto shapeClassroomRequestDto() {
        ClassroomRequestDto classroomDto = new ClassroomRequestDto();
        classroomDto.setNumber("1-B");
        classroomDto.setCapacity(20);
        classroomDto.setDescription("Кабинет химии");
        classroomDto.setTeacher(10L);
        return classroomDto;
    }

    public static ClassroomRequestDto shapeInvalidClassroomRequestDto() {
        ClassroomRequestDto classroomDto = new ClassroomRequestDto();
        classroomDto.setNumber(null);
        classroomDto.setCapacity(0);
        classroomDto.setDescription("Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химииКабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химии Кабинет химииКабинет химии Кабинет химии Кабинет химии Кабинет химии");
        classroomDto.setTeacher(null);
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
        Teacher teacher = new Teacher();
        classroom.setTeacher(teacher);
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
        TeacherResponseDto teacher = new TeacherResponseDto();
        classroomDto.setTeacher(teacher);
        return classroomDto;
    }

    public static Page<ClassroomResponseDto> shapePageOfClassroomResponseDto(int page, int size, int total) {
        List<ClassroomResponseDto> classroomResponseDtoList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            ClassroomResponseDto classroomResponseDto = shapeClassroomResponseDto();
            classroomResponseDto.setId((long) i);
            classroomResponseDto.setNumber(String.valueOf(i));
            classroomResponseDtoList.add(classroomResponseDto);
        }
        return PageableExecutionUtils.getPage(
                classroomResponseDtoList,
                PageRequest.of(page, size),
                () -> total);
    }

    public static Page<Classroom> shapePageOfClassroomEntity(int page, int size, int total) {
        List<Classroom> classrooms = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            Classroom classroom = shapeClassroomEntity();
            classroom.setId((long) i);
            classroom.setNumber(String.valueOf(i));
            classrooms.add(classroom);
        }
        return PageableExecutionUtils.getPage(
                classrooms,
                PageRequest.of(page, size),
                () -> total);
    }
}
