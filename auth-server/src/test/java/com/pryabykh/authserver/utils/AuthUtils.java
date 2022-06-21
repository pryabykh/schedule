package com.pryabykh.authserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.authserver.dtos.UserCredentialsDto;

public class AuthUtils {
    public static UserCredentialsDto shapeUserCredentialsDto() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("john@ya.ru");
        userCredentialsDto.setPassword("123456");
        return userCredentialsDto;
    }

    public static UserCredentialsDto shapeInvalidUserCredentialsDto() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("john");
        userCredentialsDto.setPassword("");
        return userCredentialsDto;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }
}
