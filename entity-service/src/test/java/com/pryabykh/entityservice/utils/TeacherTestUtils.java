package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.*;

public class TeacherTestUtils {
    public static TeacherResponseDto shapeTeacherResponseDto() {
        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        teacherResponseDto.setId(1L);
        teacherResponseDto.setFirstName("Иван");
        teacherResponseDto.setPatronymic("Иванович");
        teacherResponseDto.setLastName("Иванов");
        teacherResponseDto.setCreatorId(1L);
        teacherResponseDto.setCreatedAt(new Date());
        teacherResponseDto.setUpdatedAt(new Date());
        teacherResponseDto.setVersion(1);
        Set<ClassroomResponseDto> classrooms = new HashSet<>();
        classrooms.add(new ClassroomResponseDto());
        teacherResponseDto.setClassrooms(classrooms);
        return teacherResponseDto;
    }

    public static TeacherRequestDto shapeTeacherRequestDto() {
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setFirstName("Иван");
        teacherRequestDto.setPatronymic("Иванович");
        teacherRequestDto.setLastName("Иванов");
        teacherRequestDto.setClassrooms(new HashSet<>(List.of(1L)));
        return teacherRequestDto;
    }

    public static TeacherRequestDto shapeInvalidTeacherRequestDto() {
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setFirstName(null);
        teacherRequestDto.setPatronymic(null);
        teacherRequestDto.setLastName(null);
        teacherRequestDto.setClassrooms(null);
        return teacherRequestDto;
    }

    public static Page<TeacherResponseDto> shapePageOfTeacherResponseDto(int page, int size, int total) {
        List<TeacherResponseDto> teacherResponseDtoList = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            TeacherResponseDto teacherResponseDto = shapeTeacherResponseDto();
            teacherResponseDto.setId((long) i);
            teacherResponseDtoList.add(teacherResponseDto);
        }
        return PageableExecutionUtils.getPage(
                teacherResponseDtoList,
                PageRequest.of(page, size),
                () -> total);
    }

    public static Teacher shapeTeacherEntity() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Иван");
        teacher.setPatronymic("Иванович");
        teacher.setLastName("Иванов");
        teacher.setCreatorId(1L);
        teacher.setCreatedAt(new Date());
        teacher.setUpdatedAt(new Date());
        teacher.setVersion(1);
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        teacher.setClassrooms(new HashSet<>(List.of(classroom)));
        return teacher;
    }

    public static Page<Teacher> shapePageOfTeacherEntity(int page, int size, int total) {
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            Teacher teacher = shapeTeacherEntity();
            teacher.setId((long) i);
            teachers.add(teacher);
        }
        return PageableExecutionUtils.getPage(
                teachers,
                PageRequest.of(page, size),
                () -> total);
    }

    public static List<TeacherResponseDto> shapeListOfTeacherResponseDtos() {
        TeacherResponseDto teacherResponseDto = shapeTeacherResponseDto();
        return Collections.singletonList(teacherResponseDto);
    }

    public static List<Teacher> shapeListOfTeacherEntities() {
        Teacher teacher = shapeTeacherEntity();
        return Collections.singletonList(teacher);
    }
}
