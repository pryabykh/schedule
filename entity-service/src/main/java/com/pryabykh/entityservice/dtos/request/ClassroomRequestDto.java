package com.pryabykh.entityservice.dtos.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClassroomRequestDto {
    @NotEmpty @Size(min = 1, max = 255)
    private String number;
    @NotNull @Min(1) @Max(Integer.MAX_VALUE)
    private int capacity;
    @NotNull @Size(max = 255)
    private String description;
    @Max(Long.MAX_VALUE)
    private Long teacher;
    @NotNull @Size(max = 50)
    private Set<Long> subjects = new HashSet<>();
}
