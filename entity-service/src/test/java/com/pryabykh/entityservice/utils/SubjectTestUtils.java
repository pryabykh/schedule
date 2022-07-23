package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SubjectTestUtils {
    public static List<Subject> shapeListOfSubjectEntities() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Русский язык");
        subject.setFirstGrade(1);
        subject.setSecondGrade(1);
        subject.setThirdGrade(1);
        subject.setFourthGrade(1);
        subject.setFifthGrade(1);
        subject.setSixthGrade(1);
        subject.setSeventhGrade(1);
        subject.setEighthGrade(1);
        subject.setNinthGrade(1);
        subject.setTenthGrade(1);
        subject.setEleventhGrade(1);
        Classroom classroom = new Classroom();
        subject.setClassrooms(new HashSet<>(List.of(classroom)));
        return new ArrayList<>(Collections.singletonList(subject));
    }

    public static List<SubjectResponseDto> shapeListOfSubjectDtos() {
        SubjectResponseDto subject = new SubjectResponseDto();
        subject.setId(1L);
        subject.setName("Русский язык");
        subject.setFirstGrade(1);
        subject.setSecondGrade(1);
        subject.setThirdGrade(1);
        subject.setFourthGrade(1);
        subject.setFifthGrade(1);
        subject.setSixthGrade(1);
        subject.setSeventhGrade(1);
        subject.setEighthGrade(1);
        subject.setNinthGrade(1);
        subject.setTenthGrade(1);
        subject.setEleventhGrade(1);
        ClassroomResponseDto classroom = new ClassroomResponseDto();
        subject.setClassrooms(new HashSet<>(List.of(classroom)));
        return new ArrayList<>(Collections.singletonList(subject));
    }

    public static Subject shapeSubjectEntity() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Русский язык");
        subject.setFirstGrade(1);
        subject.setSecondGrade(1);
        subject.setThirdGrade(1);
        subject.setFourthGrade(1);
        subject.setFifthGrade(1);
        subject.setSixthGrade(1);
        subject.setSeventhGrade(1);
        subject.setEighthGrade(1);
        subject.setNinthGrade(1);
        subject.setTenthGrade(1);
        subject.setEleventhGrade(1);
        subject.setClassrooms(new HashSet<>(List.of(new Classroom())));
        return subject;
    }
}
