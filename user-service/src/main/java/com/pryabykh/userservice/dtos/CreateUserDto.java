package com.pryabykh.userservice.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserDto extends BaseUserDto {
    @NotEmpty @Size(min = 6, max = 255)
    private String password;
}
