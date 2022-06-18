package com.pryabykh.userservice.dtos;

import lombok.Data;

@Data
public class BaseUserDto {
    private String email;
    private String firstName;
    private String lastName;
}
