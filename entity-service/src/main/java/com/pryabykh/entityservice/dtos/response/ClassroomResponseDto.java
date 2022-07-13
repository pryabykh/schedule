package com.pryabykh.entityservice.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class ClassroomResponseDto {
    private Long id;
    private String number;
    private int capacity;
    private String description;
    private Long creatorId;
    private int version;
    private Date createdAt;
    private Date updatedAt;
}
