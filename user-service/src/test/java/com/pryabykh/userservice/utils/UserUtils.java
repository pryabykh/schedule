package com.pryabykh.userservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.models.User;

import java.util.Date;
import java.util.Optional;

public class UserUtils {
    public static Optional<User> shapeExistingUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@ya.ru");
        user.setPassword("$2a$10$K7shy/f3EavtQCT3rmZaYunlP9oK6rIdkAoJdJoxElwXj0UBTXFsq");
        user.setVersion(1);
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return Optional.of(user);
    }

    public static CreateUserDto shapeCreateUserDto() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("john@ya.ru");
        createUserDto.setPassword("123456");
        createUserDto.setFirstName("John");
        createUserDto.setLastName("Smith");
        return createUserDto;
    }

    public static CreateUserDto shapeInvalidCreateUserDto() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("john");
        createUserDto.setPassword("1234");
        createUserDto.setFirstName("");
        createUserDto.setLastName("");
        return createUserDto;
    }

    public static GetUserDto shapeGetUserDto() {
        GetUserDto getUserDto = new GetUserDto();
        getUserDto.setId(1L);
        getUserDto.setEmail("john@ya.ru");
        getUserDto.setVersion(1);
        getUserDto.setFirstName("John");
        getUserDto.setLastName("Smith");
        getUserDto.setCreatedAt(new Date());
        getUserDto.setUpdatedAt(new Date());
        return getUserDto;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }
}
