package com.emerson.authservice.adapters.in;

import com.emerson.authservice.application.*;
import com.emerson.authservice.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.emerson.authservice.mocks.TestConstants;
import com.emerson.authservice.mocks.TestMocks;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginUserUseCase loginUserUseCase;
    @Mock
    private RegisterUserUseCase registerUserUseCase;
    @Mock
    private ForgotPasswordUseCase forgotPasswordUseCase;
    @Mock
    private ConfirmForgotPasswordUseCase confirmForgotPasswordUseCase;
    @Mock
    private RefreshTokenUseCase refreshTokenUseCase;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
    }


    @Test
    void login_ok() {
        var req = TestMocks.validLoginRequest();
        var tokens = Map.of(TestConstants.ACCESS_TOKEN_FIELD, TestConstants.ACCESS_TOKEN);
        when(loginUserUseCase.execute(TestConstants.EMAIL, TestConstants.PASSWORD)).thenReturn(tokens);
        var resp = authController.login(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(tokens, resp.getBody());
    }

    @Test
    void register_ok() {
        var req = TestMocks.validRegisterRequest();
        var resp = authController.register(req);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        verify(registerUserUseCase).execute(TestConstants.EMAIL, TestConstants.PASSWORD, TestConstants.NAME);
    }

    @Test
    void refreshToken_ok() {
        var req = TestMocks.validRefreshTokenRequest();
        var tokens = Map.of(TestConstants.ACCESS_TOKEN_FIELD, TestConstants.ACCESS_TOKEN);
        when(refreshTokenUseCase.execute(TestConstants.REFRESH_TOKEN)).thenReturn(tokens);
        var resp = authController.refreshToken(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(tokens, resp.getBody());
    }

    @Test
    void forgotPassword_ok() {
        var req = TestMocks.validForgotPasswordRequest();
        var resp = authController.forgotPassword(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(forgotPasswordUseCase).execute(TestConstants.EMAIL);
    }

    @Test
    void confirmForgotPassword_ok() {
        var req = TestMocks.validConfirmForgotPasswordRequest();
        var resp = authController.confirmForgotPassword(req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(confirmForgotPasswordUseCase).execute(TestConstants.EMAIL, TestConstants.CONFIRMATION_CODE, TestConstants.NEW_PASSWORD);
    }

    @Test
    void login_invalidCredentials() {
        var req = TestMocks.validLoginRequest();
        when(loginUserUseCase.execute(TestConstants.EMAIL, TestConstants.PASSWORD))
            .thenThrow(new com.emerson.authservice.exception.AuthenticationException(TestConstants.INVALID_CREDENTIALS_MSG));
        Exception ex = assertThrows(com.emerson.authservice.exception.AuthenticationException.class, () -> authController.login(req));
        assertEquals(TestConstants.INVALID_CREDENTIALS_MSG, ex.getMessage());
    }

    @Test
    void register_userAlreadyExists() {
        var req = TestMocks.validRegisterRequest();
        doThrow(new com.emerson.authservice.exception.UserAlreadyExistsException(TestConstants.USER_ALREADY_EXISTS_MSG))
            .when(registerUserUseCase).execute(TestConstants.EMAIL, TestConstants.PASSWORD, TestConstants.NAME);
        Exception ex = assertThrows(com.emerson.authservice.exception.UserAlreadyExistsException.class, () -> authController.register(req));
        assertEquals(TestConstants.USER_ALREADY_EXISTS_MSG, ex.getMessage());
    }

    @Test
    void refreshToken_invalidToken() {
        var req = TestMocks.validRefreshTokenRequest();
        when(refreshTokenUseCase.execute(TestConstants.REFRESH_TOKEN))
            .thenThrow(new com.emerson.authservice.exception.AuthenticationException(TestConstants.INVALID_REFRESH_TOKEN_MSG));
        Exception ex = assertThrows(com.emerson.authservice.exception.AuthenticationException.class, () -> authController.refreshToken(req));
        assertEquals(TestConstants.INVALID_REFRESH_TOKEN_MSG, ex.getMessage());
    }

    @Test
    void register_invalidRequest_validation() {
        var req = TestMocks.invalidRegisterRequest();
        Validator validator = new LocalValidatorFactoryBean();
        ((LocalValidatorFactoryBean) validator).afterPropertiesSet();
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.EMAIL_FIELD)));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.PASSWORD_FIELD)));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.NAME_FIELD)));
    }

    @Test
    void login_invalidRequest_validation() {
        var req = TestMocks.invalidLoginRequest();
        Validator validator = new LocalValidatorFactoryBean();
        ((LocalValidatorFactoryBean) validator).afterPropertiesSet();
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.EMAIL_FIELD)));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(TestConstants.PASSWORD_FIELD)));
    }
} 