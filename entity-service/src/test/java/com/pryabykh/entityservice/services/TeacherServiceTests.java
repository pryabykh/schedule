package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.models.Teacher;
import com.pryabykh.entityservice.repositories.TeacherRepository;
import com.pryabykh.entityservice.userContext.UserContext;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class TeacherServiceTests {
    private TeacherService teacherService;
    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    public void createPositive() {
        Mockito.when(teacherRepository.save(Mockito.any()))
                .thenReturn(TeacherTestUtils.shapeTeacherEntity());
        TeacherResponseDto teacherDto = teacherService.create(TeacherTestUtils.shapeTeacherRequestDto());
        Assertions.assertNotNull(teacherDto.getId());
        Assertions.assertNotNull(teacherDto.getFirstName());
        Assertions.assertNotNull(teacherDto.getPatronymic());
        Assertions.assertNotNull(teacherDto.getLastName());
        Assertions.assertNotNull(teacherDto.getCreatorId());
        Assertions.assertNotNull(teacherDto.getClassrooms());
        Assertions.assertTrue(teacherDto.getVersion() > 0);
        Assertions.assertNotNull(teacherDto.getCreatedAt());
        Assertions.assertNotNull(teacherDto.getUpdatedAt());
    }

    @Test
    public void createThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                teacherService.create(TeacherTestUtils.shapeInvalidTeacherRequestDto()));
    }

    @Test
    public void deletePositive() {
        Long contextUserId = 11L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Teacher teacherEntity = TeacherTestUtils.shapeTeacherEntity();
        teacherEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(teacherEntity));

            teacherService.delete(1L);
            Mockito.verify(teacherRepository).deleteById(Mockito.anyLong());
        }
    }

    @Test
    public void deleteThrowsPermissionDeniedException() {
        Long contextUserId = 10L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Teacher teacherEntity = TeacherTestUtils.shapeTeacherEntity();
        teacherEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(teacherEntity));

            Assertions.assertThrows(PermissionDeniedException.class, () ->
                    teacherService.delete(1L));
        }
    }

    @Test
    public void deleteThrowsEntityNotFoundException() {
        Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                teacherService.delete(1L));
    }

    @Test
    public void deleteThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                teacherService.delete(null));
    }

    @Test
    public void fetchAllPositiveWithoutSortingAndWithoutFiltration() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy(null);
        pageSizeDto.setSortDirection(null);
        pageSizeDto.setFilterBy(null);
        pageSizeDto.setFilterValue(null);

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(teacherRepository.findAllByCreatorId(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapePageOfTeacherEntity(1, 10, 20));

            Page<TeacherResponseDto> result = teacherService.fetchAll(pageSizeDto);

            Mockito.verify(teacherRepository).findAllByCreatorId(Mockito.anyLong(), Mockito.any());
            Assertions.assertNotNull(result);
            List<TeacherResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getFirstName());
            Assertions.assertNotNull(content.get(0).getPatronymic());
            Assertions.assertNotNull(content.get(0).getLastName());
            Assertions.assertNotNull(content.get(0).getClassrooms());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithSortingAscAndWithoutFiltration() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("asc");
        pageSizeDto.setFilterBy(null);
        pageSizeDto.setFilterValue(null);

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(teacherRepository.findAllByCreatorId(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapePageOfTeacherEntity(1, 10, 20));

            Page<TeacherResponseDto> result = teacherService.fetchAll(pageSizeDto);

            Mockito.verify(teacherRepository).findAllByCreatorId(Mockito.anyLong(), Mockito.any());
            Assertions.assertNotNull(result);
            List<TeacherResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getFirstName());
            Assertions.assertNotNull(content.get(0).getPatronymic());
            Assertions.assertNotNull(content.get(0).getLastName());
            Assertions.assertNotNull(content.get(0).getClassrooms());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithSortingDescAndWithoutFiltration() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy(null);
        pageSizeDto.setFilterValue(null);

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(teacherRepository.findAllByCreatorId(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapePageOfTeacherEntity(1, 10, 20));

            Page<TeacherResponseDto> result = teacherService.fetchAll(pageSizeDto);

            Mockito.verify(teacherRepository).findAllByCreatorId(Mockito.anyLong(), Mockito.any());
            Assertions.assertNotNull(result);
            List<TeacherResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getFirstName());
            Assertions.assertNotNull(content.get(0).getPatronymic());
            Assertions.assertNotNull(content.get(0).getLastName());
            Assertions.assertNotNull(content.get(0).getClassrooms());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithIllegalSortingDirection() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("test");
        pageSizeDto.setFilterBy(null);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                teacherService.fetchAll(pageSizeDto));
    }

    @Test
    public void fetchAllPositiveWithFiltrationByFirstName() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("firstName");
        pageSizeDto.setFilterValue("test");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(teacherRepository.findByCreatorIdAndFirstNameContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapePageOfTeacherEntity(1, 10, 20));

            Page<TeacherResponseDto> result = teacherService.fetchAll(pageSizeDto);

            Mockito.verify(teacherRepository).findByCreatorIdAndFirstNameContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<TeacherResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getFirstName());
            Assertions.assertNotNull(content.get(0).getPatronymic());
            Assertions.assertNotNull(content.get(0).getLastName());
            Assertions.assertNotNull(content.get(0).getClassrooms());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithFiltrationByPatronymic() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("patronymic");
        pageSizeDto.setFilterValue("test");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(teacherRepository.findByCreatorIdAndPatronymicContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapePageOfTeacherEntity(1, 10, 20));

            Page<TeacherResponseDto> result = teacherService.fetchAll(pageSizeDto);

            Mockito.verify(teacherRepository).findByCreatorIdAndPatronymicContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<TeacherResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getFirstName());
            Assertions.assertNotNull(content.get(0).getPatronymic());
            Assertions.assertNotNull(content.get(0).getLastName());
            Assertions.assertNotNull(content.get(0).getClassrooms());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithFiltrationByLastName() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("lastName");
        pageSizeDto.setFilterValue("test");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(teacherRepository.findByCreatorIdAndLastNameContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapePageOfTeacherEntity(1, 10, 20));

            Page<TeacherResponseDto> result = teacherService.fetchAll(pageSizeDto);

            Mockito.verify(teacherRepository).findByCreatorIdAndLastNameContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<TeacherResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getFirstName());
            Assertions.assertNotNull(content.get(0).getPatronymic());
            Assertions.assertNotNull(content.get(0).getLastName());
            Assertions.assertNotNull(content.get(0).getClassrooms());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    //TODO: Test for filter by classroom (name or smth)

    @Test
    public void fetchAllPositiveWithIllegalFiltrationField() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("test");
        pageSizeDto.setFilterBy("test");
        pageSizeDto.setFilterValue("test");

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                teacherService.fetchAll(pageSizeDto));
    }

    @Test
    public void fetchAllThrowsConstraintViolationException() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setPage(null);
        pageSizeDto.setSize(null);

        Assertions.assertThrows(ConstraintViolationException.class, () ->
                teacherService.fetchAll(pageSizeDto));
    }

    @Test
    public void findByIdPositive() {
        Long contextUserId = 11L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Teacher teacherEntity = TeacherTestUtils.shapeTeacherEntity();
        teacherEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(teacherEntity));

            TeacherResponseDto teacherDto = teacherService.fetchById(1L);
            Assertions.assertNotNull(teacherDto.getId());
            Assertions.assertNotNull(teacherDto.getFirstName());
            Assertions.assertNotNull(teacherDto.getPatronymic());
            Assertions.assertNotNull(teacherDto.getLastName());
            Assertions.assertNotNull(teacherDto.getCreatorId());
            Assertions.assertNotNull(teacherDto.getClassrooms());
            Assertions.assertTrue(teacherDto.getVersion() > 0);
            Assertions.assertNotNull(teacherDto.getCreatedAt());
            Assertions.assertNotNull(teacherDto.getUpdatedAt());
        }
    }

    @Test
    public void findByIdThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                teacherService.fetchById(null));
    }

    @Test
    public void findByIdThrowsPermissionDeniedException() {
        Long contextUserId = 10L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Teacher teacherEntity = TeacherTestUtils.shapeTeacherEntity();
        teacherEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(teacherEntity));

            Assertions.assertThrows(PermissionDeniedException.class, () ->
                    teacherService.fetchById(1L));
        }
    }

    @Test
    public void findByIdThrowsEntityNotFoundException() {
        Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                teacherService.fetchById(1L));
    }

    @Test
    public void updatePositive() {
        Long contextUserId = 11L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Teacher teacherEntity = TeacherTestUtils.shapeTeacherEntity();
        teacherEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(teacherEntity));
            Mockito.when(teacherRepository.save(Mockito.any()))
                    .thenReturn(teacherEntity);
            TeacherResponseDto teacherDto = teacherService.update(1L, TeacherTestUtils.shapeTeacherRequestDto());
            Assertions.assertNotNull(teacherDto.getId());
            Assertions.assertNotNull(teacherDto.getFirstName());
            Assertions.assertNotNull(teacherDto.getPatronymic());
            Assertions.assertNotNull(teacherDto.getLastName());
            Assertions.assertNotNull(teacherDto.getCreatorId());
            Assertions.assertNotNull(teacherDto.getClassrooms());
            Assertions.assertTrue(teacherDto.getVersion() > 0);
            Assertions.assertNotNull(teacherDto.getCreatedAt());
            Assertions.assertNotNull(teacherDto.getUpdatedAt());
        }
    }

    @Test
    public void updateThrowsPermissionDeniedException() {
        Long contextUserId = 10L;
        Long entityUserId = 11L;
        UserContext userContext = TestUtils.shapeUserContext();
        userContext.setUserId(contextUserId);
        Teacher teacherEntity = TeacherTestUtils.shapeTeacherEntity();
        teacherEntity.setCreatorId(entityUserId);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(TeacherTestUtils.shapeTeacherEntity()));
            Mockito.when(teacherRepository.save(Mockito.any()))
                    .thenReturn(TeacherTestUtils.shapeTeacherEntity());

            Assertions.assertThrows(PermissionDeniedException.class, () ->
                    teacherService.update(1L, TeacherTestUtils.shapeTeacherRequestDto()));
        }
    }

    @Test
    public void updateThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                teacherService.update(null, TeacherTestUtils.shapeInvalidTeacherRequestDto()));
    }

    @Test
    public void updateThrowsEntityNotFoundException() {
        Mockito.when(teacherRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                teacherService.update(1L, TeacherTestUtils.shapeTeacherRequestDto()));
    }

    @Test
    public void fetchAllListPositive() {
        UserContext userContext = TestUtils.shapeUserContext();
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(teacherRepository.findAllByCreatorId(Mockito.anyLong()))
                    .thenReturn(TeacherTestUtils.shapeListOfTeacherEntities());

            List<TeacherResponseDto> teachers = teacherService.fetchAllList();
            Assertions.assertNotNull(teachers);
            Assertions.assertTrue(teachers.size() > 0);
            Assertions.assertNotNull(teachers.get(0).getId());
            Assertions.assertNotNull(teachers.get(0).getFirstName());
            Assertions.assertNotNull(teachers.get(0).getPatronymic());
            Assertions.assertNotNull(teachers.get(0).getLastName());
            Assertions.assertTrue(teachers.get(0).getVersion() > 0);
            Assertions.assertNotNull(teachers.get(0).getUpdatedAt());
            Assertions.assertNotNull(teachers.get(0).getCreatedAt());
            Assertions.assertNotNull(teachers.get(0).getCreatorId());
            Assertions.assertNotNull(teachers.get(0).getClassrooms());
        }
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
}
