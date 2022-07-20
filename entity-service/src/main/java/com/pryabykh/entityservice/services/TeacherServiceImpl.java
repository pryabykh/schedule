package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import com.pryabykh.entityservice.dtos.request.TeacherRequestDto;
import com.pryabykh.entityservice.dtos.response.TeacherResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Override
    public TeacherResponseDto create(TeacherRequestDto teacherDto) {
        return null;
    }

    @Override
    public Page<TeacherResponseDto> fetchAll(PageSizeDto pageSizeDto) {
        return null;
    }

    @Override
    public TeacherResponseDto fetchById(Long id) {
        return null;
    }

    @Override
    public TeacherResponseDto update(Long id, TeacherRequestDto teacherDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
