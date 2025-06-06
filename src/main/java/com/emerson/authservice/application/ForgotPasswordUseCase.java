package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordUseCase {
    private final AuthService authService;

    public void execute(String email) {
        authService.forgotPassword(email);
    }
} 