package com.pryabykh.entityservice.services;

import com.pryabykh.entityservice.dtos.response.SubjectResponseDto;
import com.pryabykh.entityservice.repositories.SubjectRepository;
import com.pryabykh.entityservice.utils.SubjectTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class SubjectServiceTests {
    private SubjectService subjectService;
    @MockBean
    private SubjectRepository subjectRepository;

    @Test
    public void fetchAllPositive() {
        Mockito.when(subjectRepository.findAll())
                .thenReturn(SubjectTestUtils.shapeListOfSubjectEntities());

        List<SubjectResponseDto> subjects = subjectService.fetchAll();
        Assertions.assertNotNull(subjects);
        Assertions.assertTrue(subjects.size() > 0);
        Assertions.assertNotNull(subjects.get(0).getFirstGrade());
        Assertions.assertNotNull(subjects.get(0).getSecondGrade());
        Assertions.assertNotNull(subjects.get(0).getThirdGrade());
        Assertions.assertNotNull(subjects.get(0).getFourthGrade());
        Assertions.assertNotNull(subjects.get(0).getFifthGrade());
        Assertions.assertNotNull(subjects.get(0).getSixthGrade());
        Assertions.assertNotNull(subjects.get(0).getSeventhGrade());
        Assertions.assertNotNull(subjects.get(0).getEighthGrade());
        Assertions.assertNotNull(subjects.get(0).getNinthGrade());
        Assertions.assertNotNull(subjects.get(0).getTenthGrade());
        Assertions.assertNotNull(subjects.get(0).getEleventhGrade());
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
}
