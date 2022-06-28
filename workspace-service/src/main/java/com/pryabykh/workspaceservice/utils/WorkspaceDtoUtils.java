package com.pryabykh.workspaceservice.utils;

import com.pryabykh.workspaceservice.dtos.request.WorkspaceDto;
import com.pryabykh.workspaceservice.dtos.response.SavedWorkspaceDto;
import com.pryabykh.workspaceservice.models.Workspace;

public class WorkspaceDtoUtils {
    public static Workspace convertToEntity(WorkspaceDto workspaceDto, Long creatorId) {
        Workspace workspace = new Workspace();
        workspace.setName(workspaceDto.getName());
        workspace.setCreator(creatorId);
        return workspace;
    }

    public static SavedWorkspaceDto convertFromEntity(Workspace workspace) {
        SavedWorkspaceDto savedWorkspaceDto = new SavedWorkspaceDto();
        savedWorkspaceDto.setId(workspace.getId());
        savedWorkspaceDto.setName(workspace.getName());
        savedWorkspaceDto.setCreator(workspace.getCreator());
        savedWorkspaceDto.setVersion(workspace.getVersion());
        savedWorkspaceDto.setCreatedAt(workspace.getCreatedAt());
        savedWorkspaceDto.setUpdatedAt(workspace.getUpdatedAt());
        return savedWorkspaceDto;
    }
}
