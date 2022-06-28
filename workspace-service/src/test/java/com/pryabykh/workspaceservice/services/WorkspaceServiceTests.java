package com.pryabykh.workspaceservice.services;

import com.pryabykh.workspaceservice.dtos.response.SavedWorkspaceDto;
import com.pryabykh.workspaceservice.repositories.WorkspaceRepository;
import com.pryabykh.workspaceservice.utils.WorkspaceTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

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

        SavedWorkspaceDto savedWorkspaceDto = workspaceService.create(WorkspaceTestUtils.shapeWorkspaceDto());
        Assertions.assertNotNull(savedWorkspaceDto.getId());
        Assertions.assertNotNull(savedWorkspaceDto.getCreator());
        Assertions.assertNotNull(savedWorkspaceDto.getName());
        Assertions.assertNotNull(savedWorkspaceDto.getCreatedAt());
        Assertions.assertNotNull(savedWorkspaceDto.getUpdatedAt());
        Assertions.assertTrue(savedWorkspaceDto.getVersion() > 0);
    }
    //TODO: User can create only 5 workspaces
    //TODO: User send invalid data and receive Exception

    @Autowired
    public void setWorkspaceService(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }
}
