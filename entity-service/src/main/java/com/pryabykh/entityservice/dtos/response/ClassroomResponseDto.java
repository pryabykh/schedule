package com.pryabykh.entityservice.dtos.response;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClassroomResponseDto {
    private Long id;
    private String number;
    private int capacity;
    private String description;
    private Long creatorId;
    private TeacherResponseDto teacher;
    private Set<SubjectResponseDto> subjects = new HashSet<>();
    private int version;
    private Date createdAt;
    private Date updatedAt;
}
