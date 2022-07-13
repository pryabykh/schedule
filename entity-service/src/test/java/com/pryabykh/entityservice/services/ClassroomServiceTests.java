package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.ClassroomAlreadyExistsException;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.utils.ClassroomTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class ClassroomServiceTests {
    private ClassroomService classroomService;
    @MockBean
    private ClassroomRepository classroomRepository;

    @Test
    public void createPositive() {
        Mockito.when(classroomRepository.findByNumber(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(classroomRepository.save(Mockito.any()))
                .thenReturn(ClassroomTestUtils.shapeClassroomEntity());
        ClassroomResponseDto classroomDto = classroomService.create(ClassroomTestUtils.shapeClassroomRequestDto());
        Assertions.assertNotNull(classroomDto.getId());
        Assertions.assertNotNull(classroomDto.getNumber());
        Assertions.assertTrue(classroomDto.getCapacity() > 0);
        Assertions.assertNotNull(classroomDto.getDescription());
        Assertions.assertTrue(classroomDto.getVersion() > 0);
        Assertions.assertNotNull(classroomDto.getCreatedAt());
        Assertions.assertNotNull(classroomDto.getUpdatedAt());
    }

    @Test
    public void createThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                classroomService.create(ClassroomTestUtils.shapeInvalidClassroomRequestDto()));
    }

    @Test
    public void createThrowsClassroomAlreadyExistsException() {
        Mockito.when(classroomRepository.findByNumber(Mockito.anyString()))
                .thenReturn(Optional.of(ClassroomTestUtils.shapeClassroomEntity()));

        Assertions.assertThrows(ClassroomAlreadyExistsException.class, () ->
                classroomService.create(ClassroomTestUtils.shapeClassroomRequestDto()));
    }

    @Autowired
    public void setClassroomService(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }
}
