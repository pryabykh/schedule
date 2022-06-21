package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.exceptions.UserNotFoundException;
import com.pryabykh.userservice.models.User;
import com.pryabykh.userservice.repositories.UserRepository;
import com.pryabykh.userservice.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles({"test", "postgresql" , "eureka", "liquibase"})
public class UserServiceTests {
    private UserService userService;
    private BCryptPasswordEncoder encoder;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void registerPositive() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());
        User existingUser = UserUtils.shapeExistingUserEntity().orElseThrow(IllegalArgumentException::new);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(existingUser);

        GetUserDto registeredUser = userService.register(UserUtils.shapeCreateUserDto());

        Assertions.assertEquals(registeredUser.getId(), existingUser.getId());
        Assertions.assertEquals(registeredUser.getCreatedAt(), existingUser.getCreatedAt());
        Assertions.assertEquals(registeredUser.getUpdatedAt(), existingUser.getUpdatedAt());
        Assertions.assertEquals(registeredUser.getEmail(), existingUser.getEmail());
        Assertions.assertEquals(registeredUser.getVersion(), existingUser.getVersion());
        Assertions.assertEquals(registeredUser.getFirstName(), existingUser.getFirstName());
        Assertions.assertEquals(registeredUser.getLastName(), existingUser.getLastName());
    }

    @Test
    public void registerThrowsUserAlreadyExistsException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(UserUtils.shapeExistingUserEntity());
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.register(UserUtils.shapeCreateUserDto()));
    }

    @Test
    public void registerThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                userService.register(UserUtils.shapeInvalidCreateUserDto()));
    }

    @Test
    public void checkCredentialsTrue() {
        String password = "123456";
        String passwordHash = encoder.encode(password);
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(UserUtils.shapeExistingUserEntityByPassword(passwordHash));

        boolean result = userService.checkCredentials(UserUtils.shapeUserCredentialsDtoByPassword(password));

        Assertions.assertTrue(result);
    }

    @Test
    public void checkCredentialsFalse() {
        String givenPassword = "givenPassowrd123";
        String actualPassword = "123456";
        String actualPasswordHash = encoder.encode(actualPassword);
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(UserUtils.shapeExistingUserEntityByPassword(actualPasswordHash));

        boolean result = userService.checkCredentials(UserUtils.shapeUserCredentialsDtoByPassword(givenPassword));

        Assertions.assertFalse(result);
    }

    @Test
    public void checkCredentialsUserNotFound() {
        String password = "123456";
        String passwordHash = encoder.encode(password);
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.checkCredentials(UserUtils.shapeUserCredentialsDtoByPassword(password)));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }
}
