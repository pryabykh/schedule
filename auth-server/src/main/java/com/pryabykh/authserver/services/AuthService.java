package com.pryabykh.authserver.services;

import com.pryabykh.authserver.dtos.LoginResultDto;
import com.pryabykh.authserver.dtos.UserCredentialsDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface AuthService {
    LoginResultDto login(@Valid UserCredentialsDto userCredentialsDto);
}
