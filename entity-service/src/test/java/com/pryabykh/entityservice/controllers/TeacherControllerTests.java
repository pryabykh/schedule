package com.pryabykh.entityservice.controllers;

import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.services.TeacherService;
import com.pryabykh.entityservice.utils.TeacherTestUtils;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@ActiveProfiles("test")
public class TeacherControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;

    @Test
    public void createPositive() throws Exception {
        Mockito.when(teacherService.create(Mockito.any()))
                .thenReturn(TeacherTestUtils.shapeTeacherResponseDto());

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(post("/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void createInvalidRequest() throws Exception {
        Mockito.when(teacherService.create(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeInvalidTeacherRequestDto();
        mockMvc.perform(post("/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createInternalServerError() throws Exception {
        Mockito.when(teacherService.create(Mockito.any()))
                .thenThrow(RuntimeException.class);

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(post("/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deletePositive() throws Exception {
        mockMvc.perform(delete("/v1/teachers/1"))
                .andExpect(status().isOk());
        Mockito.verify(teacherService).delete(Mockito.anyLong());
    }

    @Test
    public void deletePermissionDenied() throws Exception {
        Mockito.doThrow(PermissionDeniedException.class)
                .when(teacherService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/teachers/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteNonExistingTeacher() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class)
                .when(teacherService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/teachers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteInvalidId() throws Exception {
        Mockito.doThrow(ConstraintViolationException.class)
                .when(teacherService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/teachers/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteInternalServerError() throws Exception {
        Mockito.doThrow(RuntimeException.class)
                .when(teacherService).delete(Mockito.anyLong());

        mockMvc.perform(delete("/v1/teachers/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void fetchAllPositive() throws Exception {
        Mockito.when(teacherService.fetchAll(Mockito.any()))
                .thenReturn(TeacherTestUtils.shapePageOfTeacherResponseDto(1, 10, 20));

        mockMvc.perform(post("/v1/teachers/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(TestUtils.shapePageSizeDto(0, 10))))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchAllInvalidDto() throws Exception {
        Mockito.when(teacherService.fetchAll(Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(post("/v1/teachers/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(TestUtils.shapePageSizeDto(0, 10))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchAllInternalServerError() throws Exception {
        Mockito.when(teacherService.fetchAll(Mockito.any()))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(post("/v1/teachers/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(TestUtils.shapePageSizeDto(0, 10))))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void fetchByIdPositive() throws Exception {
        Mockito.when(teacherService.fetchById(Mockito.anyLong()))
                .thenReturn(TeacherTestUtils.shapeTeacherResponseDto());

        mockMvc.perform(get("/v1/teachers/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchByIdPermissionDenied() throws Exception {
        Mockito.when(teacherService.fetchById(Mockito.anyLong()))
                .thenThrow(PermissionDeniedException.class);

        mockMvc.perform(get("/v1/teachers/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void fetchByIdEntityNotFound() throws Exception {
        Mockito.when(teacherService.fetchById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/v1/teachers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fetchByIdBadRequest() throws Exception {
        Mockito.when(teacherService.fetchById(Mockito.anyLong()))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(get("/v1/teachers/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchByIdInternalServerError() throws Exception {
        Mockito.when(teacherService.fetchById(Mockito.anyLong()))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(get("/v1/teachers/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updatePositive() throws Exception {
        Mockito.when(teacherService.update(Mockito.anyLong(), Mockito.any()))
                .thenReturn(TeacherTestUtils.shapeTeacherResponseDto());

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(put("/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePermissionDenied() throws Exception {
        Mockito.when(teacherService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(PermissionDeniedException.class);

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(put("/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateBadRequest() throws Exception {
        Mockito.when(teacherService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(ConstraintViolationException.class);

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(put("/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEntityNotFound() throws Exception {
        Mockito.when(teacherService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(put("/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateInternalServerError() throws Exception {
        Mockito.when(teacherService.update(Mockito.anyLong(), Mockito.any()))
                .thenThrow(RuntimeException.class);

        TeacherRequestDto teacherRequestDto = TeacherTestUtils.shapeTeacherRequestDto();
        mockMvc.perform(put("/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.toJson(teacherRequestDto)))
                .andExpect(status().isInternalServerError());
    }
}
