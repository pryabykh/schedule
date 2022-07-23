package com.pryabykh.entityservice.dtos.response;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SubjectResponseDto {
    private Long id;
    private String name;
    private Integer firstGrade;
    private Integer secondGrade;
    private Integer thirdGrade;
    private Integer fourthGrade;
    private Integer fifthGrade;
    private Integer sixthGrade;
    private Integer seventhGrade;
    private Integer eighthGrade;
    private Integer ninthGrade;
    private Integer tenthGrade;
    private Integer eleventhGrade;
    private Set<ClassroomResponseDto> classrooms = new HashSet<>();
}
