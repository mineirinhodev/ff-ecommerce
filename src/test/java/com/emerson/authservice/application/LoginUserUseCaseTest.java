package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.emerson.authservice.mocks.TestConstants;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_ok() {
        Map<String, String> tokens = Map.of("accessToken", TestConstants.ACCESS_TOKEN);
        when(authService.login(TestConstants.EMAIL, TestConstants.PASSWORD)).thenReturn(tokens);
        Map<String, String> result = loginUserUseCase.execute(TestConstants.EMAIL, TestConstants.PASSWORD);
        assertEquals(tokens, result);
    }

    @Test
    void execute_invalidCredentials() {
        when(authService.login(TestConstants.EMAIL, TestConstants.PASSWORD)).thenThrow(new com.emerson.authservice.exception.AuthenticationException(TestConstants.INVALID_CREDENTIALS_MSG));
        Exception ex = assertThrows(com.emerson.authservice.exception.AuthenticationException.class, () -> loginUserUseCase.execute(TestConstants.EMAIL, TestConstants.PASSWORD));
        assertEquals(TestConstants.INVALID_CREDENTIALS_MSG, ex.getMessage());
    }
} 