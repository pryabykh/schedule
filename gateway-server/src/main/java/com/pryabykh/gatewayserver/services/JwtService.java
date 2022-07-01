package com.pryabykh.gatewayserver.services;

public interface JwtService {
    boolean isTokenValid(String token);
    String getUserId(String token);
    String getUserEmail(String token);
}
