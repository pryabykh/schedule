package com.pryabykh.userservice.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserDto extends BaseUserDto {
    private String password;
}
