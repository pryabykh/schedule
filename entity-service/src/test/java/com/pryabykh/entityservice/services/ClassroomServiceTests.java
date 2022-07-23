package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.exceptions.EntityAlreadyExistsException;
import com.pryabykh.entityservice.exceptions.EntityNotFoundException;
import com.pryabykh.entityservice.exceptions.PermissionDeniedException;
import com.pryabykh.entityservice.models.Classroom;
import com.pryabykh.entityservice.repositories.ClassroomRepository;
import com.pryabykh.entityservice.repositories.SubjectRepository;
import com.pryabykh.entityservice.repositories.TeacherRepository;
import com.pryabykh.entityservice.userContext.UserContext;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.ClassroomTestUtils;
import com.pryabykh.entityservice.utils.SubjectTestUtils;
import com.pryabykh.entityservice.utils.TeacherTestUtils;
import com.pryabykh.entityservice.utils.TestUtils;
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
public class ClassroomServiceTests {
    private ClassroomService classroomService;
    @MockBean
    private ClassroomRepository classroomRepository;
    @MockBean
    private SubjectRepository subjectRepository;
    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    public void createPositive() {
        UserContext userContext = TestUtils.shapeUserContext();
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(userContext);
            Mockito.when(classroomRepository.findByNumberAndCreatorId(Mockito.anyString(), Mockito.anyLong()))
                    .thenReturn(Optional.empty());
            Mockito.when(classroomRepository.save(Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapeClassroomEntity());
            Mockito.when(subjectRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(SubjectTestUtils.shapeSubjectEntity()));
            Mockito.when(teacherRepository.findByIdAndCreatorId(Mockito.anyLong(), Mockito.anyLong()))
                    .thenReturn(Optional.of(TeacherTestUtils.shapeTeacherEntity()));
            ClassroomResponseDto classroomDto = classroomService.create(ClassroomTestUtils.shapeClassroomRequestDto());
            Assertions.assertNotNull(classroomDto.getId());
            Assertions.assertNotNull(classroomDto.getNumber());
            Assertions.assertTrue(classroomDto.getCapacity() > 0);
            Assertions.assertNotNull(classroomDto.getDescription());
            Assertions.assertNotNull(classroomDto.getTeacher());
            Assertions.assertNotNull(classroomDto.getSubjects());
            Assertions.assertTrue(classroomDto.getVersion() > 0);
            Assertions.assertNotNull(classroomDto.getCreatedAt());
            Assertions.assertNotNull(classroomDto.getUpdatedAt());
        }

    }

    @Test
    public void createThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                classroomService.create(ClassroomTestUtils.shapeInvalidClassroomRequestDto()));
    }

    @Test
    public void createThrowsClassroomAlreadyExistsException() {
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(classroomRepository.findByNumberAndCreatorId(Mockito.anyString(), Mockito.anyLong()))
                    .thenReturn(Optional.of(ClassroomTestUtils.shapeClassroomEntity()));

            Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                    classroomService.create(ClassroomTestUtils.shapeClassroomRequestDto()));
        }
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
            Mockito.when(classroomRepository.findAllByCreatorId(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findAllByCreatorId(Mockito.anyLong(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
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
            Mockito.when(classroomRepository.findAllByCreatorId(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findAllByCreatorId(Mockito.anyLong(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
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
            Mockito.when(classroomRepository.findAllByCreatorId(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findAllByCreatorId(Mockito.anyLong(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
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
                classroomService.fetchAll(pageSizeDto));
    }

    @Test
    public void fetchAllPositiveWithFiltrationByNumber() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("number");
        pageSizeDto.setFilterValue("test");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(classroomRepository.findByCreatorIdAndNumberContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findByCreatorIdAndNumberContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithFiltrationByCapacity() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("capacity");
        pageSizeDto.setFilterValue("15");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(classroomRepository.findByCreatorIdAndCapacityContaining(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findByCreatorIdAndCapacityContaining(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithFiltrationByDescription() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("description");
        pageSizeDto.setFilterValue("test");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(classroomRepository.findByCreatorIdAndDescriptionContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findByCreatorIdAndDescriptionContainingIgnoreCase(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithFiltrationByTeacher() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("desc");
        pageSizeDto.setFilterBy("teacher");
        pageSizeDto.setFilterValue("test");

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
            Mockito.when(classroomRepository.findByCreatorIdAndTeacherIdContaining(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapePageOfClassroomEntity(1, 10, 20));

            Page<ClassroomResponseDto> result = classroomService.fetchAll(pageSizeDto);

            Mockito.verify(classroomRepository).findByCreatorIdAndTeacherIdContaining(Mockito.anyLong(), Mockito.anyString(), Mockito.any());
            Assertions.assertNotNull(result);
            List<ClassroomResponseDto> content = result.getContent();
            Assertions.assertNotNull(content.get(0).getId());
            Assertions.assertNotNull(content.get(0).getCreatorId());
            Assertions.assertNotNull(content.get(0).getNumber());
            Assertions.assertNotNull(content.get(0).getTeacher());
            Assertions.assertNotNull(content.get(0).getSubjects());
            Assertions.assertTrue(content.get(0).getCapacity() > 0);
            Assertions.assertNotNull(content.get(0).getDescription());
            Assertions.assertTrue(content.get(0).getVersion() > 0);
            Assertions.assertNotNull(content.get(0).getCreatedAt());
            Assertions.assertNotNull(content.get(0).getUpdatedAt());
        }
    }

    @Test
    public void fetchAllPositiveWithIllegalFiltrationField() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setSortBy("id");
        pageSizeDto.setSortDirection("test");
        pageSizeDto.setFilterBy("test");
        pageSizeDto.setFilterValue("test");

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                classroomService.fetchAll(pageSizeDto));
    }

    @Test
    public void fetchAllThrowsConstraintViolationException() {
        PageSizeDto pageSizeDto = TestUtils.shapePageSizeDto(1, 10);
        pageSizeDto.setPage(null);
        pageSizeDto.setSize(null);

        Assertions.assertThrows(ConstraintViolationException.class, () ->
                classroomService.fetchAll(pageSizeDto));
    }

    @Test
    public void findByIdPositive() {
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

            ClassroomResponseDto classroomDto = classroomService.fetchById(1L);
            Assertions.assertNotNull(classroomDto.getId());
            Assertions.assertNotNull(classroomDto.getNumber());
            Assertions.assertTrue(classroomDto.getCapacity() > 0);
            Assertions.assertNotNull(classroomDto.getTeacher());
            Assertions.assertNotNull(classroomDto.getSubjects());
            Assertions.assertNotNull(classroomDto.getDescription());
            Assertions.assertTrue(classroomDto.getVersion() > 0);
            Assertions.assertNotNull(classroomDto.getCreatedAt());
            Assertions.assertNotNull(classroomDto.getUpdatedAt());
        }
    }

    @Test
    public void findByIdThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                classroomService.fetchById(null));
    }

    @Test
    public void findByIdThrowsPermissionDeniedException() {
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
                    classroomService.fetchById(1L));
        }
    }

    @Test
    public void findByIdThrowsEntityNotFoundException() {
        Mockito.when(classroomRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                classroomService.fetchById(1L));
    }

    @Test
    public void updatePositive() {
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
            Mockito.when(classroomRepository.findByNumberAndCreatorId(Mockito.anyString(), Mockito.anyLong()))
                    .thenReturn(Optional.empty());
            Mockito.when(classroomRepository.save(Mockito.any()))
                    .thenReturn(classroomEntity);
            Mockito.when(subjectRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(SubjectTestUtils.shapeSubjectEntity()));
            Mockito.when(teacherRepository.findByIdAndCreatorId(Mockito.anyLong(), Mockito.anyLong()))
                    .thenReturn(Optional.of(TeacherTestUtils.shapeTeacherEntity()));
            ClassroomResponseDto classroomDto = classroomService.update(1L, ClassroomTestUtils.shapeClassroomRequestDto());
            Assertions.assertNotNull(classroomDto.getId());
            Assertions.assertNotNull(classroomDto.getNumber());
            Assertions.assertTrue(classroomDto.getCapacity() > 0);
            Assertions.assertNotNull(classroomDto.getTeacher());
            Assertions.assertNotNull(classroomDto.getSubjects());
            Assertions.assertNotNull(classroomDto.getDescription());
            Assertions.assertTrue(classroomDto.getVersion() > 0);
            Assertions.assertNotNull(classroomDto.getCreatedAt());
            Assertions.assertNotNull(classroomDto.getUpdatedAt());
        }
    }

    @Test
    public void updateThrowsPermissionDeniedException() {
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
                    .thenReturn(Optional.of(ClassroomTestUtils.shapeClassroomEntity()));
            Mockito.when(classroomRepository.findByNumberAndCreatorId(Mockito.anyString(), Mockito.anyLong()))
                    .thenReturn(Optional.empty());
            Mockito.when(classroomRepository.save(Mockito.any()))
                    .thenReturn(ClassroomTestUtils.shapeClassroomEntity());

            Assertions.assertThrows(PermissionDeniedException.class, () ->
                    classroomService.update(1L, ClassroomTestUtils.shapeClassroomRequestDto()));
        }
    }

    @Test
    public void updateThrowsEntityAlreadyExistsException() {
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
            Mockito.when(classroomRepository.findByNumberAndCreatorId(Mockito.anyString(), Mockito.anyLong()))
                    .thenReturn(Optional.of(classroomEntity));
            Mockito.when(classroomRepository.save(Mockito.any()))
                    .thenReturn(classroomEntity);

            ClassroomRequestDto classroomRequestDto = ClassroomTestUtils.shapeClassroomRequestDto();
            classroomRequestDto.setNumber("new number");
            Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                    classroomService.update(1L, classroomRequestDto));
        }
    }

    @Test
    public void updateThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                classroomService.update(null, ClassroomTestUtils.shapeInvalidClassroomRequestDto()));
    }

    @Test
    public void updateThrowsEntityNotFoundException() {
        Mockito.when(classroomRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                classroomService.update(1L, ClassroomTestUtils.shapeClassroomRequestDto()));
    }

    @Autowired
    public void setClassroomService(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }
}
