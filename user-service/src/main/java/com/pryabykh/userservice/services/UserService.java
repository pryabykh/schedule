package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.dtos.UserCredentialsDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Validated
public interface UserService {
    GetUserDto register(@Valid CreateUserDto userDto);
    boolean checkCredentials(@Valid UserCredentialsDto userCredentialsDto);
    Long findUserIdByEmail(@NotEmpty @Email String email);
}
