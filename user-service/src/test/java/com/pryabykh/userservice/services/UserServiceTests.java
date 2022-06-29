package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.exceptions.UserNotFoundException;
import com.pryabykh.userservice.models.User;
import com.pryabykh.userservice.repositories.UserRepository;
import com.pryabykh.userservice.utils.UserTestUtils;
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
@ActiveProfiles("test")
public class UserServiceTests {
    private UserService userService;
    private BCryptPasswordEncoder encoder;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void registerPositive() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());
        User existingUser = UserTestUtils.shapeExistingUserEntity().orElseThrow(IllegalArgumentException::new);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(existingUser);

        GetUserDto registeredUser = userService.register(UserTestUtils.shapeCreateUserDto());

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
                .thenReturn(UserTestUtils.shapeExistingUserEntity());
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.register(UserTestUtils.shapeCreateUserDto()));
    }

    @Test
    public void registerThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                userService.register(UserTestUtils.shapeInvalidCreateUserDto()));
    }

    @Test
    public void checkCredentialsTrue() {
        String password = "123456";
        String passwordHash = encoder.encode(password);
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(UserTestUtils.shapeExistingUserEntityByPassword(passwordHash));

        boolean result = userService.checkCredentials(UserTestUtils.shapeUserCredentialsDtoByPassword(password));

        Assertions.assertTrue(result);
    }

    @Test
    public void checkCredentialsFalse() {
        String givenPassword = "givenPassowrd123";
        String actualPassword = "123456";
        String actualPasswordHash = encoder.encode(actualPassword);
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(UserTestUtils.shapeExistingUserEntityByPassword(actualPasswordHash));

        boolean result = userService.checkCredentials(UserTestUtils.shapeUserCredentialsDtoByPassword(givenPassword));

        Assertions.assertFalse(result);
    }

    @Test
    public void checkCredentialsThrowsUserNotFoundException() {
        String password = "123456";
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());
        boolean result = userService.checkCredentials(UserTestUtils.shapeUserCredentialsDtoByPassword(password));
        Assertions.assertFalse(result);
    }

    @Test
    public void checkCredentialsThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                userService.checkCredentials(UserTestUtils.shapeInvalidUserCredentialsDto()));
    }

    @Test
    public void findUserIdByEmailPositive() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(UserTestUtils.shapeExistingUserEntity());

        Long userId = userService.findUserIdByEmail("test@ya.ru");
        Assertions.assertNotNull(userId);
    }

    @Test
    public void findUserIdByEmailThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                userService.findUserIdByEmail(""));
    }

    @Test
    public void findUserIdByEmailThrowsUserNotFoundException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.findUserIdByEmail("test@ya.ru"));
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
