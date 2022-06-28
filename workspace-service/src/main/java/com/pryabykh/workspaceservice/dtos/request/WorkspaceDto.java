package com.pryabykh.workspaceservice.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class WorkspaceDto {
    @NotEmpty @Size(min = 1, max = 255)
    private String name;
}
