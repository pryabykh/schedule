package com.pryabykh.gatewayserver.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtVerifierConfiguration {
    @Value("${auth.jwt.secret-key}")
    private String tokenSecretKey;

    @Bean
    public JWTVerifier jwtVerifier() {
        Algorithm algorithm = Algorithm.HMAC256(tokenSecretKey);
        return JWT.require(algorithm).build();
    }
}
