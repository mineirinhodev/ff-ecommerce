package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.emerson.authservice.mocks.TestConstants;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_ok() {
        registerUserUseCase.execute(TestConstants.EMAIL, TestConstants.PASSWORD, TestConstants.NAME);
        verify(authService).register(TestConstants.EMAIL, TestConstants.PASSWORD, TestConstants.NAME);
    }
} 