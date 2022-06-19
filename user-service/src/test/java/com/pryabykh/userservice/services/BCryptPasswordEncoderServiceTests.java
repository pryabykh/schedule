package com.pryabykh.userservice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderServiceTests {
    private BCryptPasswordEncoder encoder;
    private BCryptPasswordEncoderService encoderService;

    @BeforeEach
    public void construct() {
        encoder = new BCryptPasswordEncoder();
        encoderService = new BCryptPasswordEncoderServiceImpl(encoder);
    }

    @Test
    public void generateHash() {
        String password = "123Qwe";
        String actualHash = encoderService.generateHash(password);
        Assertions.assertTrue(encoder.matches(password, actualHash));
    }
}
