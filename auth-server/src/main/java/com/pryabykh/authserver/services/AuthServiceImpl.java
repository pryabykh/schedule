package com.pryabykh.authserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pryabykh.authserver.dtos.LoginResultDto;
import com.pryabykh.authserver.dtos.UserCredentialsDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.feign.UserServiceFeignClient;
import com.pryabykh.authserver.models.Token;
import com.pryabykh.authserver.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserServiceFeignClient userServiceFeignClient;
    private final TokenRepository tokenRepository;
    @Value("${auth.jwt.secret-key}")
    private String tokenSecretKey;
    @Value("${auth.jwt.user-claim-name}")
    private String userClaimName;
    @Value("${auth.jwt.access-token.expiresInMinutes}")
    private long tokenExpiresInMinutes;
    @Value("${auth.jwt.refresh-token.expiresInMinutes}")
    private long refreshExpiresInMinutes;

    public AuthServiceImpl(UserServiceFeignClient userServiceFeignClient, TokenRepository tokenRepository) {
        this.userServiceFeignClient = userServiceFeignClient;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public LoginResultDto login(UserCredentialsDto userCredentialsDto) {
        boolean userAuthenticated = userServiceFeignClient.checkCredentials(userCredentialsDto);
        if (!userAuthenticated) {
            throw new BadCredentialsException();
        }
        LoginResultDto loginResultDto = new LoginResultDto();
        loginResultDto.setAccessToken(createAccessToken(userCredentialsDto.getEmail()));
        loginResultDto.setExpiresIn(tokenExpiresInMinutes * 60);
        loginResultDto.setRefreshToken(createRefreshToken(userCredentialsDto.getEmail()));
        loginResultDto.setRefreshExpiresIn(refreshExpiresInMinutes * 60);
        return loginResultDto;
    }

    private String createAccessToken(String userEmail) {
        return createToken(userEmail, tokenExpiresInMinutes);
    }

    private String createRefreshToken(String userEmail) {
        tokenRepository.removeByEmail(userEmail);
        tokenRepository.flush();
        String refreshToken = createToken(userEmail, refreshExpiresInMinutes);
        Token token = new Token();
        token.setToken(refreshToken);
        token.setEmail(userEmail);
        tokenRepository.save(token);
        return refreshToken;
    }

    private String createToken(String userEmail, long expiresInMinutes) {
        Algorithm algorithm = Algorithm.HMAC256(tokenSecretKey);
        Date now = new Date();
        return JWT.create()
                .withExpiresAt(new Date(now.getTime() + (expiresInMinutes * 60 * 1000)))
                .withClaim(userClaimName, userEmail)
                .sign(algorithm);
    }
}
