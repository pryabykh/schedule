package com.pryabykh.entityservice.dtos.request;

import com.pryabykh.entityservice.models.Classroom;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class TeacherRequestDto {
    @NotEmpty @Size(min = 1, max = 255)
    private String firstName;
    @NotEmpty @Size(min = 1, max = 255)
    private String patronymic;
    @NotEmpty @Size(min = 1, max = 255)
    private String lastName;
    @Size(min = 1, max = 50)
    private Set<Long> classrooms = new HashSet<>();
}
