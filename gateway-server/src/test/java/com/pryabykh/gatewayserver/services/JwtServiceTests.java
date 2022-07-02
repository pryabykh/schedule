package com.pryabykh.gatewayserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
public class JwtServiceTests {
    private JwtService jwtService;
    private Algorithm algorithm;
    @Value("${auth.jwt.secret-key}")
    private String tokenSecretKey;
    @Value("${auth.jwt.user-email-claim-name}")
    private String userEmailClaimName;
    @Value("${auth.jwt.user-id-claim-name}")
    private String userIdClaimName;

    @BeforeEach
    public void beforeAll() {
        algorithm = Algorithm.HMAC256(tokenSecretKey);
    }

    @Test
    public void tokenIsValid() {
        String token = JWT.create()
                .withExpiresAt(new Date(new Date().getTime() + (15 * 60 * 1000)))
                .withClaim(userIdClaimName, "1")
                .withClaim(userEmailClaimName, "test@email.com")
                .sign(algorithm);
        boolean result = jwtService.isTokenValid(token);
        Assertions.assertTrue(result);
    }

    @Test
    public void tokenIsInvalid() {
        String token = JWT.create()
                .withExpiresAt(new Date(new Date().getTime() - (15 * 60 * 1000)))
                .withClaim(userIdClaimName, "1")
                .withClaim(userEmailClaimName, "test@email.com")
                .sign(algorithm);
        boolean result = jwtService.isTokenValid(token);
        Assertions.assertFalse(result);
    }

    @Test
    public void getUserId() {
        String userIdForToken = "1";
        String token = JWT.create()
                .withExpiresAt(new Date(new Date().getTime() + (15 * 60 * 1000)))
                .withClaim(userIdClaimName, userIdForToken)
                .withClaim(userEmailClaimName, "test@email.com")
                .sign(algorithm);
        String userIdResult = jwtService.getUserId(token);
        Assertions.assertEquals(userIdResult, userIdForToken);
    }

    @Test
    public void getUserEmail() {
        String userEmailForToken = "test@email.com";
        String token = JWT.create()
                .withExpiresAt(new Date(new Date().getTime() + (15 * 60 * 1000)))
                .withClaim(userIdClaimName, "1")
                .withClaim(userEmailClaimName, userEmailForToken)
                .sign(algorithm);
        String userEmailResult = jwtService.getUserEmail(token);
        Assertions.assertEquals(userEmailResult, userEmailForToken);
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
