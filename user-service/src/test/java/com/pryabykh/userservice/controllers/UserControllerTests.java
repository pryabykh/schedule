package com.pryabykh.userservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.UserCredentialsDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.exceptions.UserNotFoundException;
import com.pryabykh.userservice.services.UserService;
import com.pryabykh.userservice.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles({"test", "postgresql" , "eureka", "liquibase"})
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void registerPositive() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenReturn(UserUtils.shapeGetUserDto());

        CreateUserDto createUserDto = UserUtils.shapeCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(createUserDto.getEmail())))
                .andExpect(jsonPath("$.firstName", equalTo(createUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(createUserDto.getLastName())));
    }

    @Test
    public void registerInvalidRequest() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        CreateUserDto createUserDto = UserUtils.shapeInvalidCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserAlreadyExists() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenThrow(UserAlreadyExistsException.class);

        CreateUserDto createUserDto = UserUtils.shapeCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerInternalServerError() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenThrow(RuntimeException.class);

        CreateUserDto createUserDto = UserUtils.shapeCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(createUserDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void checkCredentialsPositive() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenReturn(true);

        UserCredentialsDto userCredentialsDto = UserUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(userCredentialsDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void checkCredentialsInvalidRequest() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        UserCredentialsDto userCredentialsDto = UserUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(userCredentialsDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkCredentialsUserNotFound() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenThrow(UserNotFoundException.class);

        UserCredentialsDto userCredentialsDto = UserUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(userCredentialsDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkCredentialsInternalError() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenThrow(RuntimeException.class);

        UserCredentialsDto userCredentialsDto = UserUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserUtils.toJson(userCredentialsDto)))
                .andExpect(status().isInternalServerError());
    }
}
