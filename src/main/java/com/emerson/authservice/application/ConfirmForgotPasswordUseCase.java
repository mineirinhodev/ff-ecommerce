package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmForgotPasswordUseCase {
    private final AuthService authService;

    public void execute(String email, String code, String newPassword) {
        authService.confirmForgotPassword(email, code, newPassword);
    }
} 