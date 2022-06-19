package com.pryabykh.userservice.services;

import com.pryabykh.userservice.dtos.GetUserDto;
import com.pryabykh.userservice.exceptions.UserAlreadyExistsException;
import com.pryabykh.userservice.models.User;
import com.pryabykh.userservice.repositories.UserRepository;
import com.pryabykh.userservice.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoderService encoderService;

    @Test
    public void registerPositive() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(encoderService.generateHash(Mockito.anyString()))
                .thenReturn("$2a$10$K7shy/f3EavtQCT3rmZaYunlP9oK6rIdkAoJdJoxElwXj0UBTXFsq");
        Optional<User> user = UserUtils.shapeExistingUserEntity();
        User existingUser = user.orElseThrow(IllegalArgumentException::new);
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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
