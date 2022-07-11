package com.pryabykh.entityservice.controllers;

import com.pryabykh.entityservice.services.SubjectService;
import com.pryabykh.entityservice.userContext.UserContextHolder;
import com.pryabykh.entityservice.utils.SubjectTestUtils;
import com.pryabykh.entityservice.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(SubjectController.class)
@ActiveProfiles("test")
public class SubjectControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectService subjectService;

    @Test
    public void fetchAllPositive() throws Exception {
        Mockito.when(subjectService.fetchAll())
                .thenReturn(SubjectTestUtils.shapeListOfSubjectDtos());

        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
        }

        mockMvc.perform(get("/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].firstGrade", notNullValue()))
                .andExpect(jsonPath("$[0].secondGrade", notNullValue()))
                .andExpect(jsonPath("$[0].thirdGrade", notNullValue()))
                .andExpect(jsonPath("$[0].fourthGrade", notNullValue()))
                .andExpect(jsonPath("$[0].fifthGrade", notNullValue()))
                .andExpect(jsonPath("$[0].sixthGrade", notNullValue()))
                .andExpect(jsonPath("$[0].seventhGrade", notNullValue()))
                .andExpect(jsonPath("$[0].eighthGrade", notNullValue()))
                .andExpect(jsonPath("$[0].ninthGrade", notNullValue()))
                .andExpect(jsonPath("$[0].tenthGrade", notNullValue()))
                .andExpect(jsonPath("$[0].eleventhGrade", notNullValue()));
    }

    @Test
    public void fetchAllInternalError() throws Exception {
        Mockito.when(subjectService.fetchAll())
                .thenThrow(RuntimeException.class);
        try (MockedStatic<UserContextHolder> userContextHolderMocked = Mockito.mockStatic(UserContextHolder.class)) {
            userContextHolderMocked.when(UserContextHolder::getContext)
                    .thenReturn(TestUtils.shapeUserContext());
        }

        mockMvc.perform(get("/v1/subjects"))
                .andExpect(status().isInternalServerError());
    }
}
