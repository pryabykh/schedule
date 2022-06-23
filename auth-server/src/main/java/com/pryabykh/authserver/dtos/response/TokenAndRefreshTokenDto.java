package com.pryabykh.authserver.dtos.response;

import lombok.Data;

@Data
public class TokenAndRefreshTokenDto {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private long refreshExpiresIn;
}
