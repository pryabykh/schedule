package com.pryabykh.entityservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.entityservice.userContext.UserContext;

public class TestUtils {
    public static UserContext shapeUserContext() {
        UserContext userContext = new UserContext();
        userContext.setUserId(5L);
        userContext.setUserEmail("test@test.ru");
        return userContext;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }
}
