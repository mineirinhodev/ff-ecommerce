package com.emerson.authservice.application;

import com.emerson.authservice.domain.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final AuthService authService;

    public Map<String, String> execute(String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
} 