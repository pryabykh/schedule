package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.models.User;
import com.pryabykh.userservice.repositories.UserRepository;
import com.pryabykh.userservice.utils.UserDtoUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetUserDto register(CreateUserDto userDto) {
        if (userAlreadyExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        User userEntity = UserDtoUtils.convertToEntity(userDto);
        User savedEntity = userRepository.save(userEntity);
        return UserDtoUtils.convertFromEntity(savedEntity, GetUserDto.class);
    }

    boolean userAlreadyExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }
}
