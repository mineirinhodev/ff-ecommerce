package com.emerson.authservice.controller;

import com.emerson.authservice.dto.ConfirmForgotPasswordRequest;
import com.emerson.authservice.dto.ForgotPasswordRequest;
import com.emerson.authservice.dto.LoginRequest;
import com.emerson.authservice.dto.RegisterRequest;
import com.emerson.authservice.service.CognitoAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CognitoAuthService authService;

    public AuthController(CognitoAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Map<String, String> tokens = authService.login(String.valueOf(request.getEmail()), request.getPassword());
        return ResponseEntity.ok(tokens);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Código de redefinição enviado para seu email.");
    }

    @PostMapping("/confirm-forgot-password")
    public ResponseEntity<String> confirmForgotPassword(@RequestBody @Valid ConfirmForgotPasswordRequest request) {
        authService.confirmForgotPassword(
                request.getEmail(),
                request.getConfirmationCode(),
                request.getNewPassword()
        );
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}