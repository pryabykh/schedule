package com.pryabykh.userservice.controllers;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.UserCredentialsDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.exceptions.UserNotFoundException;
import com.pryabykh.userservice.services.UserService;
import com.pryabykh.userservice.userContext.UserContextHolder;
import com.pryabykh.userservice.utils.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void registerPositive() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenReturn(UserTestUtils.shapeGetUserDto());
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        CreateUserDto createUserDto = UserTestUtils.shapeCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(createUserDto.getEmail())))
                .andExpect(jsonPath("$.firstName", equalTo(createUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(createUserDto.getLastName())));
    }

    @Test
    public void registerInvalidRequest() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        CreateUserDto createUserDto = UserTestUtils.shapeInvalidCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserAlreadyExists() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenThrow(UserAlreadyExistsException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        CreateUserDto createUserDto = UserTestUtils.shapeCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(createUserDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerInternalServerError() throws Exception {
        Mockito.when(userService.register(Mockito.any()))
                .thenThrow(RuntimeException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        CreateUserDto createUserDto = UserTestUtils.shapeCreateUserDto();
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(createUserDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void checkCredentialsPositive() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenReturn(true);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void checkCredentialsInvalidRequest() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkCredentialsUserNotFound() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenThrow(UserNotFoundException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkCredentialsInternalError() throws Exception {
        Mockito.when(userService.checkCredentials(Mockito.any()))
                .thenThrow(RuntimeException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(post("/v1/users/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserTestUtils.toJson(userCredentialsDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void findUserIdByEmailPositive() throws Exception {
        Mockito.when(userService.findUserIdByEmail(Mockito.anyString()))
                .thenReturn(1L);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(get("/v1/users/findIdByEmail/test@ya.ru"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void findUserIdByEmailInvalidRequest() throws Exception {
        Mockito.when(userService.findUserIdByEmail(Mockito.anyString()))
                .thenThrow(ConstraintViolationException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(get("/v1/users/findIdByEmail/test@ya.ru"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findUserIdByEmailUserNotFound() throws Exception {
        Mockito.when(userService.findUserIdByEmail(Mockito.anyString()))
                .thenThrow(UserNotFoundException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(get("/v1/users/findIdByEmail/test@ya.ru"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserIdByEmailInternalServerError() throws Exception {
        Mockito.when(userService.findUserIdByEmail(Mockito.anyString()))
                .thenThrow(RuntimeException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(UserTestUtils.shapeEmptyUserContext());
        }

        UserCredentialsDto userCredentialsDto = UserTestUtils.shapeUserCredentialsDtoByPassword("123456");
        mockMvc.perform(get("/v1/users/findIdByEmail/test@ya.ru"))
                .andExpect(status().isInternalServerError());
    }
}
