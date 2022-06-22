package com.pryabykh.authserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.authserver.dtos.UserCredentialsDto;
import com.pryabykh.authserver.models.Token;

import java.util.Date;

public class AuthTestUtils {
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

    public static Token shapeSavedToken() {
        Token token = new Token();
        token.setId(1L);
        token.setToken("123456789");
        token.setEmail("john@ya.ru");
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        return token;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }
}
