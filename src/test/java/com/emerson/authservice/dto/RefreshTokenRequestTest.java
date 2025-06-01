package com.emerson.authservice.dto;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.emerson.authservice.constants.TestConstants.REFRESH_TOKEN;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenRefreshTokenIsNull_thenViolation() {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(null)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O refresh token é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void whenRefreshTokenIsBlank_thenViolation() {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken("")
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O refresh token é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void whenRefreshTokenIsWhitespace_thenViolation() {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken("   ")
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O refresh token é obrigatório", violations.iterator().next().getMessage());
    }
} 