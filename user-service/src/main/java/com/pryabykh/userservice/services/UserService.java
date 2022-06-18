package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;

public interface UserService {
    GetUserDto register(CreateUserDto userDto);
}
