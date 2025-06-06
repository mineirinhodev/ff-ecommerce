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
class ForgotPasswordUseCaseTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private ForgotPasswordUseCase forgotPasswordUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_ok() {
        forgotPasswordUseCase.execute(TestConstants.EMAIL);
        verify(authService).forgotPassword(TestConstants.EMAIL);
    }
} 