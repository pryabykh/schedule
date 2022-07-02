package com.pryabykh.gatewayserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    private JWTVerifier jwtVerifier;
    @Value("${auth.jwt.user-email-claim-name}")
    private String userEmailClaimName;
    @Value("${auth.jwt.user-id-claim-name}")
    private String userIdClaimName;

    public JwtServiceImpl(JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public String getUserId(String token) {
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim(userIdClaimName).asLong().toString();
    }

    @Override
    public String getUserEmail(String token) {
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim(userEmailClaimName).asString();
    }
}
