package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {
    private final AuthService authService;

    public Map<String, String> execute(String email, String password) {
        return authService.login(email, password);
    }
} 