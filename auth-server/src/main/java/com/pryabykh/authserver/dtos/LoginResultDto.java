package com.pryabykh.authserver.dtos;

import lombok.Data;

@Data
public class LoginResultDto {
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private int refreshExpiresIn;
}
