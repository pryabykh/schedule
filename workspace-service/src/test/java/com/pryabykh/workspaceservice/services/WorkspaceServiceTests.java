package com.pryabykh.workspaceservice.services;

import com.pryabykh.workspaceservice.dtos.response.SavedWorkspaceDto;
import com.pryabykh.workspaceservice.exceptions.UserExhaustedLimitForWorkspacesException;
import com.pryabykh.workspaceservice.repositories.WorkspaceRepository;
import com.pryabykh.workspaceservice.utils.UserContextHolder;
import com.pryabykh.workspaceservice.utils.WorkspaceTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
public class WorkspaceServiceTests {
    private WorkspaceService workspaceService;
    @MockBean
    private WorkspaceRepository workspaceRepository;

    @Test
    public void createPositive() {
        Mockito.when(workspaceRepository.save(Mockito.any()))
                .thenReturn(WorkspaceTestUtils.shapeSavedWorkspace());
        Mockito.when(workspaceRepository.countByCreator(Mockito.anyLong()))
                .thenReturn(1L);

        SavedWorkspaceDto savedWorkspaceDto = workspaceService.create(WorkspaceTestUtils.shapeWorkspaceDto());
        Assertions.assertNotNull(savedWorkspaceDto.getId());
        Assertions.assertNotNull(savedWorkspaceDto.getCreator());
        Assertions.assertNotNull(savedWorkspaceDto.getName());
        Assertions.assertNotNull(savedWorkspaceDto.getCreatedAt());
        Assertions.assertNotNull(savedWorkspaceDto.getUpdatedAt());
        Assertions.assertTrue(savedWorkspaceDto.getVersion() > 0);
    }

    @Test
    public void createThrowsUserExhaustedLimitForWorkspaces() {
        Mockito.when(workspaceRepository.countByCreator(Mockito.anyLong()))
                .thenReturn(5L);
        MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class);
        userContextHolderMocked.when(UserContextHolder::getContext)
                        .thenReturn(WorkspaceTestUtils.shapeUserContext());

        Assertions.assertThrows(UserExhaustedLimitForWorkspacesException.class, () ->
                workspaceService.create(WorkspaceTestUtils.shapeWorkspaceDto()));
    }

    @Test
    public void createThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                workspaceService.create(WorkspaceTestUtils.shapeInvalidWorkspaceDto()));
    }

    @Autowired
    public void setWorkspaceService(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }
}
