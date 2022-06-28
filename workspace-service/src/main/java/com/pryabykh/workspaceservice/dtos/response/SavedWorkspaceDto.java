package com.pryabykh.workspaceservice.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class SavedWorkspaceDto {
    private Long id;
    private String name;
    private Long creator;
    private int version;
    private Date createdAt;
    private Date updatedAt;
}
