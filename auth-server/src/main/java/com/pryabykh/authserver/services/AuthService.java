package com.pryabykh.authserver.services;

import com.pryabykh.authserver.dtos.request.RefreshTokenDto;
import com.pryabykh.authserver.dtos.response.TokenAndRefreshTokenDto;
import com.pryabykh.authserver.dtos.request.UserCredentialsDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface AuthService {
    TokenAndRefreshTokenDto login(@Valid UserCredentialsDto userCredentialsDto);
    TokenAndRefreshTokenDto refresh(@Valid RefreshTokenDto refreshTokenDto);
}
