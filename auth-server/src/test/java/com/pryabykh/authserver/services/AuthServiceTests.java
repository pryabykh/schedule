package com.pryabykh.authserver.services;

import com.pryabykh.authserver.dtos.response.TokenAndRefreshTokenDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.exceptions.InvalidTokenException;
import com.pryabykh.authserver.exceptions.TokenDoesNotExistException;
import com.pryabykh.authserver.feign.UserServiceFeignClient;
import com.pryabykh.authserver.repositories.TokenRepository;
import com.pryabykh.authserver.utils.AuthTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTests {
    private AuthService authService;
    @MockBean
    private UserServiceFeignClient userServiceFeignClient;
    @MockBean
    private TokenRepository tokenRepository;
    @Value("${auth.jwt.secret-key}")
    private String tokenSecretKey;

    @Test
    public void loginPositive() {
        Mockito.when(userServiceFeignClient.checkCredentials(Mockito.any()))
                .thenReturn(true);
        Mockito.when(userServiceFeignClient.findUserIdByEmail(Mockito.anyString()))
                .thenReturn(1L);
        Mockito.when(tokenRepository.save(Mockito.any()))
                .thenReturn(AuthTestUtils.shapeSavedToken());

        TokenAndRefreshTokenDto loginResult = authService.login(AuthTestUtils.shapeUserCredentialsDto());
        Assertions.assertNotNull(loginResult.getAccessToken());
        Assertions.assertNotNull(loginResult.getRefreshToken());
        Assertions.assertTrue(loginResult.getExpiresIn() > 0);
        Assertions.assertTrue(loginResult.getRefreshExpiresIn() > 0);
    }

    @Test
    public void loginThrowsBadCredentialsException() {
        Mockito.when(userServiceFeignClient.checkCredentials(Mockito.any()))
                .thenReturn(false);

        Assertions.assertThrows(BadCredentialsException.class, () ->
                authService.login(AuthTestUtils.shapeUserCredentialsDto()));
    }

    @Test
    public void loginThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                authService.login(AuthTestUtils.shapeInvalidUserCredentialsDto()));
    }

    @Test
    public void refreshPositive() {
        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(AuthTestUtils.shapeSavedToken()));
        Mockito.when(userServiceFeignClient.findUserIdByEmail(Mockito.anyString()))
                .thenReturn(1L);
        Mockito.when(tokenRepository.save(Mockito.any()))
                .thenReturn(AuthTestUtils.shapeSavedToken());

        TokenAndRefreshTokenDto refresh = authService.refresh(AuthTestUtils.shapeRefreshTokenDto(tokenSecretKey));
        Assertions.assertNotNull(refresh.getAccessToken());
        Assertions.assertNotNull(refresh.getRefreshToken());
        Assertions.assertTrue(refresh.getExpiresIn() > 0);
        Assertions.assertTrue(refresh.getRefreshExpiresIn() > 0);
    }

    @Test
    public void refreshThrowsConstraintViolationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                authService.refresh(AuthTestUtils.shapeEmptyRefreshTokenDto()));
    }

    @Test
    public void refreshThrowsTokenDoesNotExistException() {
        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(TokenDoesNotExistException.class, () ->
                authService.refresh(AuthTestUtils.shapeRefreshTokenDto(tokenSecretKey)));
    }

    @Test
    public void refreshThrowsInvalidTokenException() {
        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(AuthTestUtils.shapeSavedToken()));
        Assertions.assertThrows(InvalidTokenException.class, () ->
                authService.refresh(AuthTestUtils.shapeInvalidRefreshTokenDto()));
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

}
