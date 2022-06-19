package com.pryabykh.userservice.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserDto extends BaseUserDto {
    private Long id;
    private int version;
    private Date createdAt;
    private Date updatedAt;
}
