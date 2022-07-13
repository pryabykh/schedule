package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.EntityAlreadyExistsException;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.userContext.UserContext;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.ClassroomTestUtils;
import com.pryabykh.entityservice.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
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

        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                classroomService.create(ClassroomTestUtils.shapeClassroomRequestDto()));
    }

    @Test
    public void deletePositive() {
        Long contextUserId = 11L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Classroom classroomEntity = ClassroomTestUtils.shapeClassroomEntity();
        classroomEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(classroomRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(classroomEntity));

            classroomService.delete(1L);
            Mockito.verify(classroomRepository).deleteById(Mockito.anyLong());
        }
    }

    @Test
    public void deleteThrowsPermissionDeniedException() {
        Long contextUserId = 10L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Classroom classroomEntity = ClassroomTestUtils.shapeClassroomEntity();
        classroomEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(classroomRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(classroomEntity));

            Assertions.assertThrows(PermissionDeniedException.class, () ->
                    classroomService.delete(1L));
        }
    }

    @Test
    public void deleteThrowsEntityNotFoundException() {
        Mockito.when(classroomRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                classroomService.delete(1L));
    }

    @Test
    public void deleteThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                classroomService.delete(null));
    }

    @Autowired
    public void setClassroomService(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }
}
