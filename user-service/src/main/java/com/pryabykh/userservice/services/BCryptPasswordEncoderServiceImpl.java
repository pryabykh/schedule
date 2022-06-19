package com.pryabykh.userservice.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordEncoderServiceImpl implements BCryptPasswordEncoderService {
    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordEncoderServiceImpl(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String generateHash(String password) {
        return encoder.encode(password);
    }
}
