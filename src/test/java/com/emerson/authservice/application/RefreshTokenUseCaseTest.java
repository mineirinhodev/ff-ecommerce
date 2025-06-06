package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import com.emerson.authservice.mocks.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_ok() {
        Map<String, String> tokens = Map.of(TestConstants.ACCESS_TOKEN_FIELD, TestConstants.ACCESS_TOKEN);
        when(authService.refreshToken(TestConstants.REFRESH_TOKEN)).thenReturn(tokens);
        Map<String, String> result = refreshTokenUseCase.execute(TestConstants.REFRESH_TOKEN);
        assertEquals(tokens, result);
    }
} 