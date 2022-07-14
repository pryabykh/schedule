package com.pryabykh.entityservice.controllers;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.exceptions.EntityAlreadyExistsException;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.services.ClassroomService;
import com.pryabykh.entityservice.utils.ClassroomTestUtils;
import com.pryabykh.entityservice.utils.TestUtils;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void createInvalidRequest() throws Exception {
        Mockito.when(classroomService.create(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeInvalidClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createClassroomAlreadyExists() throws Exception {
        Mockito.when(classroomService.create(Mockito.any()))
                .thenThrow(EntityAlreadyExistsException.class);

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

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(post("/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deletePositive() throws Exception {
        mockMvc.perform(delete("/v1/classrooms/1"))
                .andExpect(status().isOk());
        Mockito.verify(classroomService).delete(Mockito.anyLong());
    }

    @Test
    public void deletePermissionDenied() throws Exception {
        Mockito.doThrow(PermissionDeniedException.class)
                        .when(classroomService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/classrooms/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteNonExistingClassroom() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class)
                .when(classroomService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/classrooms/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteInvalidId() throws Exception {
        Mockito.doThrow(ConstraintViolationException.class)
                .when(classroomService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/classrooms/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteInternalServerError() throws Exception {
        Mockito.doThrow(RuntimeException.class)
                .when(classroomService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/classrooms/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void fetchAllPositive() throws Exception {
        Mockito.when(classroomService.fetchAll(Mockito.any()))
                .thenReturn(ClassroomTestUtils.shapePageOfClassroomResponseDto(1, 10, 20));

        mockMvc.perform(post("/v1/classrooms/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(TestUtils.shapePageSizeDto(0, 10))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].number", notNullValue()))
                .andExpect(jsonPath("$.content[0].capacity", notNullValue()))
                .andExpect(jsonPath("$.content[0].description", notNullValue()))
                .andExpect(jsonPath("$.content[0].creatorId", notNullValue()))
                .andExpect(jsonPath("$.content[0].version", notNullValue()))
                .andExpect(jsonPath("$.content[0].createdAt", notNullValue()))
                .andExpect(jsonPath("$.content[0].updatedAt", notNullValue()));
    }

    @Test
    public void fetchAllInvalidDto() throws Exception {
        Mockito.when(classroomService.fetchAll(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(post("/v1/classrooms/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(TestUtils.shapePageSizeDto(0, 10))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchAllInternalServerError() throws Exception {
        Mockito.when(classroomService.fetchAll(Mockito.any()))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(post("/v1/classrooms/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(TestUtils.shapePageSizeDto(0, 10))))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void fetchByIdPositive() throws Exception {
        Mockito.when(classroomService.fetchById(Mockito.anyLong()))
                .thenReturn(ClassroomTestUtils.shapeClassroomResponseDto());

        mockMvc.perform(get("/v1/classrooms/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchByIdPermissionDenied() throws Exception {
        Mockito.when(classroomService.fetchById(Mockito.anyLong()))
                .thenThrow(PermissionDeniedException.class);

        mockMvc.perform(get("/v1/classrooms/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void fetchByIdEntityNotFound() throws Exception {
        Mockito.when(classroomService.fetchById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/v1/classrooms/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fetchByIdBadRequest() throws Exception {
        Mockito.when(classroomService.fetchById(Mockito.anyLong()))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(get("/v1/classrooms/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchByIdInternalServerError() throws Exception {
        Mockito.when(classroomService.fetchById(Mockito.anyLong()))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(get("/v1/classrooms/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updatePositive() throws Exception {
        Mockito.when(classroomService.update(Mockito.anyLong(), Mockito.any()))
                .thenReturn(ClassroomTestUtils.shapeClassroomResponseDto());

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(put("/v1/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePermissionDenied() throws Exception {
        Mockito.when(classroomService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(PermissionDeniedException.class);

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(put("/v1/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateBadRequest() throws Exception {
        Mockito.when(classroomService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(put("/v1/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEntityNotFound() throws Exception {
        Mockito.when(classroomService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(put("/v1/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateInternalServerError() throws Exception {
        Mockito.when(classroomService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(RuntimeException.class);

        ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
        mockMvc.perform(put("/v1/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(classroomRequestDto)))
                .andExpect(status().isInternalServerError());
    }
}
