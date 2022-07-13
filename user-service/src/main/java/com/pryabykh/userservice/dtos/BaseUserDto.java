package com.pryabykh.userservice.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class BaseUserDto {
    @NotEmpty @Email @Size(min = 1, max = 255)
    private String email;
    @NotEmpty @Size(min = 1, max = 255)
    private String firstName;
    @NotEmpty @Size(min = 1, max = 255)
    private String lastName;
}
