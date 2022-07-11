package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.response.SubjectDto;
import com.pryabykh.entityservice.models.Subject;

public class SubjectDtoUtils {
    public static SubjectDto convertFromEntity(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
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
