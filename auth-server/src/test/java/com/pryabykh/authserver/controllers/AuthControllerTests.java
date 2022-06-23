package com.pryabykh.authserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pryabykh.authserver.dtos.UserCredentialsDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.services.AuthService;
import com.pryabykh.authserver.utils.AuthTestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    public void loginPositive() throws Exception {
        Mockito.when(authService.login(Mockito.any()))
                .thenReturn(AuthTestUtils.shapeLoginResultDto());

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
}
