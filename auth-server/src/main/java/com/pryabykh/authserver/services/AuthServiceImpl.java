package com.pryabykh.authserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.pryabykh.authserver.dtos.request.RefreshTokenDto;
import com.pryabykh.authserver.dtos.response.TokenAndRefreshTokenDto;
import com.pryabykh.authserver.dtos.request.UserCredentialsDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.exceptions.InvalidTokenException;
import com.pryabykh.authserver.exceptions.TokenDoesNotExistException;
import com.pryabykh.authserver.feign.UserServiceFeignClient;
import com.pryabykh.authserver.models.Token;
import com.pryabykh.authserver.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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
    public TokenAndRefreshTokenDto login(UserCredentialsDto userCredentialsDto) {
        boolean userAuthenticated = userServiceFeignClient.checkCredentials(userCredentialsDto);
        if (!userAuthenticated) {
            throw new BadCredentialsException();
        }
        String userEmail = userCredentialsDto.getEmail();
        String accessToken = createAccessToken(userEmail);
        String refreshToken = createRefreshToken(userEmail);
        return createTokenAndRefreshTokenDto(accessToken, refreshToken);
    }

    @Override
    public TokenAndRefreshTokenDto refresh(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        Optional<Token> optionalToken = tokenRepository.findByToken(refreshToken);
        optionalToken.orElseThrow(TokenDoesNotExistException::new);
        DecodedJWT decodedJWT = verifyToken(refreshToken);
        String userEmail = decodedJWT.getClaim(userClaimName).asString();
        String newAccessToken = createAccessToken(userEmail);
        String newRefreshToken = createRefreshToken(userEmail);
        return createTokenAndRefreshTokenDto(newAccessToken, newRefreshToken);
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

    private TokenAndRefreshTokenDto createTokenAndRefreshTokenDto(String accessToken, String refreshToken) {
        TokenAndRefreshTokenDto tokenAndRefreshTokenDto = new TokenAndRefreshTokenDto();
        tokenAndRefreshTokenDto.setAccessToken(accessToken);
        tokenAndRefreshTokenDto.setExpiresIn(tokenExpiresInMinutes * 60);
        tokenAndRefreshTokenDto.setRefreshToken(refreshToken);
        tokenAndRefreshTokenDto.setRefreshExpiresIn(refreshExpiresInMinutes * 60);
        return tokenAndRefreshTokenDto;
    }

    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(tokenSecretKey);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new InvalidTokenException();
        }
    }

}
