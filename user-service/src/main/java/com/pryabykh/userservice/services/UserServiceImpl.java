package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.dtos.UserCredentialsDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.exceptions.UserNotFoundException;
import com.pryabykh.userservice.models.User;
import com.pryabykh.userservice.repositories.UserRepository;
import com.pryabykh.userservice.utils.UserDtoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public GetUserDto register(CreateUserDto userDto) {
        String userEmail = userDto.getEmail();
        if (userAlreadyExists(userEmail)) {
            log.info("Cannot register user with this email: " + userEmail + ". User already exists");
            throw new UserAlreadyExistsException();
        }
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        User userEntity = UserDtoUtils.convertToEntity(userDto);
        User savedEntity = userRepository.save(userEntity);
        return UserDtoUtils.convertFromEntity(savedEntity, GetUserDto.class);
    }

    @Override
    public boolean checkCredentials(UserCredentialsDto userCredentialsDto) {
        User user = userRepository.findByEmail(userCredentialsDto.getEmail()).orElseThrow(UserNotFoundException::new);
        return encoder.matches(userCredentialsDto.getPassword(), user.getPassword());
    }

    boolean userAlreadyExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }
}
