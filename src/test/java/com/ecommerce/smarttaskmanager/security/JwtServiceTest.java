package com.ecommerce.smarttaskmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "mySecretKeymySecretKeymySecretKeymySecretKey");

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                3600000L);
    }

    @Test
    void shouldGenerateTokenSuccessfully() {

        String token =
                jwtService.generateToken(
                        "lakshman@gmail.com",
                        "ADMIN");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractEmailSuccessfully() {

        String token =
                jwtService.generateToken(
                        "lakshman@gmail.com",
                        "ADMIN");

        String email =
                jwtService.extractEmail(token);

        assertEquals(
                "lakshman@gmail.com",
                email);
    }

    @Test
    void shouldValidateTokenSuccessfully() {

        String token =
                jwtService.generateToken(
                        "lakshman@gmail.com",
                        "ADMIN");

        assertTrue(
                jwtService.isTokenValid(token));
    }
}