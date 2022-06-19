package com.pryabykh.userservice.utils;

import com.pryabykh.userservice.dtos.BaseUserDto;
import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.models.User;

public class UserDtoUtils {
    public static User convertToEntity(BaseUserDto baseUser) {
        if (baseUser instanceof CreateUserDto createUserDto) {
            User user = new User();
            user.setEmail(createUserDto.getEmail());
            user.setPassword(createUserDto.getPassword());
            user.setFirstName(createUserDto.getFirstName());
            user.setLastName(createUserDto.getLastName());
            return user;
        } else if (baseUser instanceof GetUserDto getUserDto) {
            User user = new User();
            user.setId(getUserDto.getId());
            user.setVersion(getUserDto.getVersion());
            user.setCreatedAt(getUserDto.getCreatedAt());
            user.setUpdatedAt(getUserDto.getUpdatedAt());
            user.setEmail(getUserDto.getEmail());
            user.setFirstName(getUserDto.getFirstName());
            user.setLastName(getUserDto.getLastName());
            return user;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <T> T convertFromEntity(User user, Class<T> clazz) {
        if (clazz.equals(CreateUserDto.class)) {
            CreateUserDto createUserDto = new CreateUserDto();
            createUserDto.setEmail(user.getEmail());
            createUserDto.setFirstName(user.getFirstName());
            createUserDto.setLastName(user.getLastName());
            createUserDto.setPassword(user.getPassword());
            return (T) createUserDto;
        } else if (clazz.equals(GetUserDto.class)) {
            GetUserDto getUserDto = new GetUserDto();
            getUserDto.setId(user.getId());
            getUserDto.setCreatedAt(user.getCreatedAt());
            getUserDto.setUpdatedAt(user.getUpdatedAt());
            getUserDto.setVersion(user.getVersion());
            getUserDto.setEmail(user.getEmail());
            getUserDto.setFirstName(user.getFirstName());
            getUserDto.setLastName(user.getLastName());
            return (T) getUserDto;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
