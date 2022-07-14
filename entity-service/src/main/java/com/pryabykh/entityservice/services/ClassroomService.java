package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
public interface ClassroomService {
    ClassroomResponseDto create(@Valid ClassroomRequestDto classroomDto);

    Page<ClassroomResponseDto> fetchAll(@Valid PageSizeDto pageSizeDto);

    ClassroomResponseDto fetchById(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id);

    ClassroomResponseDto update(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id,
                                @Valid ClassroomRequestDto classroomDto);

    void delete(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id);
}
