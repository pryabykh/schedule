package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.models.Subject;

import java.util.Set;
import java.util.stream.Collectors;

public class SubjectDtoUtils {
    public static SubjectResponseDto convertFromEntity(Subject subject) {
        if (subject == null) {
            return null;
        }
        SubjectResponseDto subjectDto = new SubjectResponseDto();
        subjectDto.setId(subject.getId());
        subjectDto.setName(subject.getName());
        subjectDto.setFirstGrade(subject.getFirstGrade());
        subjectDto.setSecondGrade(subject.getSecondGrade());
        subjectDto.setThirdGrade(subject.getThirdGrade());
        subjectDto.setFourthGrade(subject.getFourthGrade());
        subjectDto.setFifthGrade(subject.getFifthGrade());
        subjectDto.setSixthGrade(subject.getSixthGrade());
        subjectDto.setSeventhGrade(subject.getSeventhGrade());
        subjectDto.setEighthGrade(subject.getEighthGrade());
        subjectDto.setNinthGrade(subject.getNinthGrade());
        subjectDto.setTenthGrade(subject.getTenthGrade());
        subjectDto.setEleventhGrade(subject.getEleventhGrade());
        Set<ClassroomResponseDto> classroomsResponse = subject.getClassrooms()
                .stream()
                .map((ClassroomDtoUtils::convertFromEntityNoRecursion))
                .collect(Collectors.toSet());
        subjectDto.setClassrooms(classroomsResponse);
        return subjectDto;
    }

    public static SubjectResponseDto convertFromEntityNoRecursion(Subject subject) {
        if (subject == null) {
            return null;
        }
        SubjectResponseDto subjectDto = new SubjectResponseDto();
        subjectDto.setId(subject.getId());
        subjectDto.setName(subject.getName());
        subjectDto.setFirstGrade(subject.getFirstGrade());
        subjectDto.setSecondGrade(subject.getSecondGrade());
        subjectDto.setThirdGrade(subject.getThirdGrade());
        subjectDto.setFourthGrade(subject.getFourthGrade());
        subjectDto.setFifthGrade(subject.getFifthGrade());
        subjectDto.setSixthGrade(subject.getSixthGrade());
        subjectDto.setSeventhGrade(subject.getSeventhGrade());
        subjectDto.setEighthGrade(subject.getEighthGrade());
        subjectDto.setNinthGrade(subject.getNinthGrade());
        subjectDto.setTenthGrade(subject.getTenthGrade());
        subjectDto.setEleventhGrade(subject.getEleventhGrade());
        return subjectDto;
    }
}
