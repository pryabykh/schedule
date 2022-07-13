package com.pryabykh.entityservice.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ClassroomRequestDto {
    @NotEmpty @Size(min = 1, max = 255)
    private String number;
    @NotEmpty @Size(min = 1, max = 255)
    private int capacity;
    @Size(max = 255)
    private String description;
}
