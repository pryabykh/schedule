package com.pryabykh.userservice.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserCredentialsDto {
    @NotEmpty @Email
    private String email;
    @NotEmpty @Size(min = 6, max = 255)
    private String password;
}
