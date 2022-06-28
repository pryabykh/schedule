package com.pryabykh.workspaceservice.services;

import com.pryabykh.workspaceservice.dtos.request.WorkspaceDto;
import com.pryabykh.workspaceservice.dtos.response.SavedWorkspaceDto;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface WorkspaceService {
    SavedWorkspaceDto create(@Valid WorkspaceDto workspaceDto);
    SavedWorkspaceDto findById(String id);//TODO: сделать валидацию для ID
    Page<SavedWorkspaceDto> findAll();//TODO: включить в интерфейс передачу данных о странице и сортировке
    SavedWorkspaceDto update(@Valid WorkspaceDto workspaceDto);
}
