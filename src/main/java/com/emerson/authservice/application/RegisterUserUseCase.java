package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final AuthService authService;

    public void execute(String email, String password, String name) {
        authService.register(email, password, name);
    }
} 