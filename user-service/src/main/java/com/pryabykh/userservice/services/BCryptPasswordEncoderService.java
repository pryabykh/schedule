package com.pryabykh.userservice.services;

public interface BCryptPasswordEncoderService {
    String generateHash(String password);
}
