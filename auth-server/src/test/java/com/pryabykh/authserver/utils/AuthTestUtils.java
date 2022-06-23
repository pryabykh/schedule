package com.pryabykh.authserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.authserver.dtos.request.RefreshTokenDto;
import com.pryabykh.authserver.dtos.response.TokenAndRefreshTokenDto;
import com.pryabykh.authserver.dtos.request.UserCredentialsDto;
import com.pryabykh.authserver.models.Token;

import java.util.Date;

public class AuthTestUtils {
    public static UserCredentialsDto shapeUserCredentialsDto() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("john@ya.ru");
        userCredentialsDto.setPassword("123456");
        return userCredentialsDto;
    }

    public static UserCredentialsDto shapeInvalidUserCredentialsDto() {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("john");
        userCredentialsDto.setPassword("");
        return userCredentialsDto;
    }

    public static Token shapeSavedToken() {
        Token token = new Token();
        token.setId(1L);
        token.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJwYXZlbEB5YS5ydSIsImV4cCI6MTY1NTk4Nzg5MX0.EDT7406GboVSTDAoKJpTGSLEVRa6O0jp_DcjT7FU8cs");
        token.setEmail("john@ya.ru");
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        return token;
    }

    public static TokenAndRefreshTokenDto shapeLoginResultDto() {
        TokenAndRefreshTokenDto tokenAndRefreshTokenDto = new TokenAndRefreshTokenDto();
        tokenAndRefreshTokenDto.setAccessToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJwYXZlbEB5YS5ydSIsImV4cCI6MTY1NTk4Nzg5MX0.EDT7406GboVSTDAoKJpTGSLEVRa6O0jp_DcjT7FU8cs");
        tokenAndRefreshTokenDto.setRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJwYXZlbEB5YS5ydSIsImV4cCI6MTY1NTk4Nzg5MX0.EDT7406GboVSTDAoKJpTGSLEVRa6O0jp_DcjT7FU8cs");
        tokenAndRefreshTokenDto.setExpiresIn(1000L);
        tokenAndRefreshTokenDto.setRefreshExpiresIn(2000L);
        return tokenAndRefreshTokenDto;
    }

    public static RefreshTokenDto shapeRefreshTokenDto() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJwYXZlbEB5YS5ydSIsImV4cCI6MTY1NTk4Nzg5MX0.EDT7406GboVSTDAoKJpTGSLEVRa6O0jp_DcjT7FU8cs");
        return refreshTokenDto;
    }

    public static RefreshTokenDto shapeEmptyRefreshTokenDto() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken("");
        return new RefreshTokenDto();
    }

    public static RefreshTokenDto shapeInvalidRefreshTokenDto() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        Algorithm algorithm = Algorithm.HMAC256("secret");
        Date now = new Date();
                String token = JWT.create()
                .withExpiresAt(new Date(now.getTime() - (15 * 60 * 1000)))
                .withClaim("email", "user@ya.ru")
                .sign(algorithm);
        refreshTokenDto.setRefreshToken(token);
        return refreshTokenDto;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }
}
