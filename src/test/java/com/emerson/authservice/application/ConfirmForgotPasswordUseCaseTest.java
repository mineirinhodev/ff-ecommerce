package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import com.emerson.authservice.mocks.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmForgotPasswordUseCaseTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private ConfirmForgotPasswordUseCase confirmForgotPasswordUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_ok() {
        confirmForgotPasswordUseCase.execute(TestConstants.EMAIL, TestConstants.CONFIRMATION_CODE, TestConstants.NEW_PASSWORD);
        verify(authService).confirmForgotPassword(TestConstants.EMAIL, TestConstants.CONFIRMATION_CODE, TestConstants.NEW_PASSWORD);
    }
} 