package com.pryabykh.entityservice.dtos.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PageSizeDto {
    @NotNull @Max(Integer.MAX_VALUE)
    private Integer page;
    @NotNull @Max(Integer.MAX_VALUE)
    private Integer size;
    @Size(max = 255)
    private String sortBy;
    @Size(max = 255)
    private String sortDirection;
    @Size(max = 255)
    private String filterBy;
    @Size(max = 255)
    private String filterValue;
}
