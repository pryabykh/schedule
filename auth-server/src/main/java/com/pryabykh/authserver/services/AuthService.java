package com.pryabykh.authserver.services;

import com.pryabykh.authserver.dtos.LoginResultDto;
import com.pryabykh.authserver.dtos.UserCredentialsDto;

public interface AuthService {
    LoginResultDto login(UserCredentialsDto userCredentialsDto);
}
