package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface UserService {
    GetUserDto register(@Valid CreateUserDto userDto);
}
