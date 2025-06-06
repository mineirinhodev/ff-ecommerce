package com.emerson.authservice.domain;

import java.util.Map;

public interface AuthService {
    Map<String, String> login(String email, String password);
    void register(String email, String password, String name);
    Map<String, String> refreshToken(String refreshToken);
    void forgotPassword(String email);
    void confirmForgotPassword(String email, String code, String newPassword);
} 