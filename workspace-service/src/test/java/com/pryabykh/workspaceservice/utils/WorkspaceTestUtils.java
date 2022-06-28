package com.pryabykh.workspaceservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.workspaceservice.dtos.request.WorkspaceDto;
import com.pryabykh.workspaceservice.models.Workspace;

import java.util.Date;

public class WorkspaceTestUtils {
    public static WorkspaceDto shapeWorkspaceDto() {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setName("ХСОШ Тест 1");
        return workspaceDto;
    }

    public static WorkspaceDto shapeInvalidWorkspaceDto() {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setName("");
        return workspaceDto;
    }

    public static Workspace shapeSavedWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("ХСОШ Тест 2");
        workspace.setCreator(1L);
        workspace.setVersion(1);
        workspace.setCreatedAt(new Date());
        workspace.setUpdatedAt(new Date());
        return workspace;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }
}
