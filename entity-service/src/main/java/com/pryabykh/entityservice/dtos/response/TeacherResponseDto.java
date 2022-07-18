package com.pryabykh.entityservice.dtos.response;

import com.pryabykh.entityservice.models.Classroom;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class TeacherResponseDto {
    private Long id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private Set<Classroom> classrooms = new HashSet<>();
    private Long creatorId;
    private int version;
    private Date createdAt;
    private Date updatedAt;
}
