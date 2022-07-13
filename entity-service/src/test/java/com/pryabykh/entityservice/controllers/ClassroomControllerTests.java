package com.pryabykh.entityservice.controllers;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.exceptions.ClassroomAlreadyExistsException;
import com.pryabykh.entityservice.services.ClassroomService;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.ClassroomTestUtils;
import com.pryabykh.entityservice.utils.TestUtils;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClassroomController.class)
@ActiveProfiles("test")
public class ClassroomControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClassroomService classroomService;

    @Test
    public void createPositive() throws Exception {
        Mockito.when(classroomService.create(Mockito.any()))
                .thenReturn(ClassroomTestUtils.shapeClassroomResponseDto());
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
        }

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(classroomRequestDto.getNumber())))
                .andExpect(jsonPath("$.capacity", equalTo(classroomRequestDto.getCapacity())))
                .andExpect(jsonPath("$.description", equalTo(classroomRequestDto.getDescription())));
    }

    @Test
    public void createInvalidRequest() throws Exception {
        Mockito.when(classroomService.create(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
        }

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeInvalidClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createClassroomAlreadyExists() throws Exception {
        Mockito.when(classroomService.create(Mockito.any()))
                .thenThrow(ClassroomAlreadyExistsException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
        }

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void createInternalServerError() throws Exception {
        Mockito.when(classroomService.create(Mockito.any()))
                .thenThrow(RuntimeException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
        }

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isInternalServerError());
    }
}
