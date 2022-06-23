package com.pryabykh.authserver.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RefreshTokenDto {
    @NotEmpty @Size(min = 6, max = 1000)
    private String refreshToken;
}
