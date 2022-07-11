package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.response.SubjectDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface SubjectService {
    List<SubjectDto> fetchAll();
}
