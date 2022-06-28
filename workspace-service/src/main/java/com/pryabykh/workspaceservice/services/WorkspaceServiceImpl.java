package com.pryabykh.workspaceservice.services;

import com.pryabykh.workspaceservice.dtos.request.WorkspaceDto;
import com.pryabykh.workspaceservice.dtos.response.SavedWorkspaceDto;
import com.pryabykh.workspaceservice.models.Workspace;
import com.pryabykh.workspaceservice.repositories.WorkspaceRepository;
import com.pryabykh.workspaceservice.utils.WorkspaceDtoUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public SavedWorkspaceDto create(WorkspaceDto workspaceDto) {
        //TODO: creatorId из брать из запроса
        Workspace workspace = WorkspaceDtoUtils.convertToEntity(workspaceDto, 1L);
        return WorkspaceDtoUtils.convertFromEntity(workspaceRepository.save(workspace));
    }

    @Override
    public SavedWorkspaceDto findById(String id) {
        return null;
    }

    @Override
    public Page<SavedWorkspaceDto> findAll() {
        return null;
    }

    @Override
    public SavedWorkspaceDto update(WorkspaceDto workspaceDto) {
        return null;
    }
}
