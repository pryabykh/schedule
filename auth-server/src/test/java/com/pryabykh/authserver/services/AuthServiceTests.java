package com.pryabykh.authserver.services;

import com.pryabykh.authserver.dtos.LoginResultDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.feign.UserServiceFeignClient;
import com.pryabykh.authserver.repositories.TokenRepository;
import com.pryabykh.authserver.utils.AuthTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTests {
    private AuthService authService;
    @MockBean
    private UserServiceFeignClient userServiceFeignClient;
    @MockBean
    private TokenRepository tokenRepository;

    @Test
    public void loginPositive() {
        Mockito.when(userServiceFeignClient.checkCredentials(Mockito.any()))
                .thenReturn(true);
        Mockito.when(tokenRepository.save(Mockito.any()))
                .thenReturn(AuthTestUtils.shapeSavedToken());

        LoginResultDto loginResult = authService.login(AuthTestUtils.shapeUserCredentialsDto());
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

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

}
