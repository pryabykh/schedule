package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.ClassroomRequestDto;
import com.pryabykh.entityservice.dtos.response.ClassroomResponseDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface ClassroomService {
    ClassroomResponseDto create(@Valid ClassroomRequestDto classroomDto);
}
