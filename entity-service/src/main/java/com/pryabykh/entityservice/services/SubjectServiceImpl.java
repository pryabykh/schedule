package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.repositories.SubjectRepository;
import com.pryabykh.entityservice.utils.SubjectDtoUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<SubjectResponseDto> fetchAll() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectDtoUtils::convertFromEntity)
                .collect(Collectors.toList());
    }
}
