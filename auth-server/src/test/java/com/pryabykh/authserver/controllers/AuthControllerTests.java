package com.pryabykh.authserver.controllers;

import com.pryabykh.authserver.dtos.request.RefreshTokenDto;
import com.pryabykh.authserver.dtos.request.UserCredentialsDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.exceptions.InvalidTokenException;
import com.pryabykh.authserver.exceptions.TokenDoesNotExistException;
import com.pryabykh.authserver.services.AuthService;
import com.pryabykh.authserver.utils.AuthTestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @Value("${auth.jwt.secret-key}")
    private String tokenSecretKey;

    @Test
    public void loginPositive() throws Exception {
        Mockito.when(authService.login(Mockito.any()))
                .thenReturn(AuthTestUtils.shapeTokenAndRefreshTokentDto());

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.expiresIn", notNullValue()))
                .andExpect(jsonPath("$.refreshExpiresIn", notNullValue()));
    }

    @Test
    public void loginInvalidRequest() throws Exception {
        Mockito.when(authService.login(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginBadCredentials() throws Exception {
        Mockito.when(authService.login(Mockito.any()))
                .thenThrow(BadCredentialsException.class);

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginInternalServerError() throws Exception {
        Mockito.when(authService.login(Mockito.any()))
                .thenThrow(RuntimeException.class);

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void refreshPositive() throws Exception {
        Mockito.when(authService.refresh(Mockito.any()))
                .thenReturn(AuthTestUtils.shapeTokenAndRefreshTokentDto());

        RefreshTokenDto refreshTokenDto = AuthTestUtils.shapeRefreshTokenDto(tokenSecretKey);
        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(refreshTokenDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.expiresIn", notNullValue()))
                .andExpect(jsonPath("$.refreshExpiresIn", notNullValue()));
    }

    @Test
    public void refreshInvalidRequest() throws Exception {
        Mockito.when(authService.refresh(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void refreshTokenDoesNotExist() throws Exception {
        Mockito.when(authService.refresh(Mockito.any()))
                .thenThrow(TokenDoesNotExistException.class);

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshInvalidToken() throws Exception {
        Mockito.when(authService.refresh(Mockito.any()))
                .thenThrow(InvalidTokenException.class);

        UserCredentialsDto userCredentialsDto = AuthTestUtils.shapeUserCredentialsDto();
        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AuthTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isUnauthorized());
    }
}
