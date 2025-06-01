package com.emerson.authservice.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.emerson.authservice.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        RegisterRequest request = RegisterRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .build();

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEmailIsNull_thenViolation() {
        RegisterRequest request = RegisterRequest.builder()
                .email(null)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenEmailIsInvalid_thenViolation() {
        RegisterRequest request = RegisterRequest.builder()
                .email("invalid-email")
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    void whenPasswordIsNull_thenViolation() {
        RegisterRequest request = RegisterRequest.builder()
                .email(TEST_EMAIL)
                .password(null)
                .name(TEST_NAME)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenPasswordIsTooShort_thenViolation() {
        RegisterRequest request = RegisterRequest.builder()
                .email(TEST_EMAIL)
                .password("123")
                .name(TEST_NAME)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long", violations.iterator().next().getMessage());
    }

    @Test
    void whenNameIsNull_thenViolation() {
        RegisterRequest request = RegisterRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(null)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenNameIsTooShort_thenViolation() {
        RegisterRequest request = RegisterRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name("A")
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must be at least 3 characters long", violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsInvalid_thenMultipleViolations() {
        RegisterRequest request = RegisterRequest.builder()
                .email("invalid-email")
                .password("123")
                .name("A")
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }
} 