package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
public interface TeacherService {
    TeacherResponseDto create(@Valid TeacherRequestDto teacherDto);

    Page<TeacherResponseDto> fetchAll(@Valid PageSizeDto pageSizeDto);

    TeacherResponseDto fetchById(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id);

    TeacherResponseDto update(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id,
                                @Valid TeacherRequestDto teacherDto);

    void delete(@NotNull @Min(1) @Max(Long.MAX_VALUE) Long id);
}
