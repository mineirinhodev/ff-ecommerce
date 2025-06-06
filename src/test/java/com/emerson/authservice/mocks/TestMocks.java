package com.emerson.authservice.mocks;

import com.emerson.authservice.dto.*;
import java.time.Instant;

public final class TestMocks {
    public static LoginRequest validLoginRequest() {
        return LoginRequest.builder()
                .email(TestConstants.EMAIL)
                .password(TestConstants.PASSWORD)
                .build();
    }
    public static LoginRequest invalidLoginRequest() {
        return LoginRequest.builder()
                .email(TestConstants.EMPTY)
                .password(TestConstants.EMPTY)
                .build();
    }
    public static RegisterRequest validRegisterRequest() {
        return RegisterRequest.builder()
                .email(TestConstants.EMAIL)
                .password(TestConstants.PASSWORD)
                .name(TestConstants.NAME)
                .build();
    }
    public static RegisterRequest invalidRegisterRequest() {
        return RegisterRequest.builder()
                .email(TestConstants.EMPTY)
                .password(TestConstants.SHORT_PASSWORD)
                .name(TestConstants.EMPTY)
                .build();
    }
    public static RefreshTokenRequest validRefreshTokenRequest() {
        return RefreshTokenRequest.builder()
                .refreshToken(TestConstants.REFRESH_TOKEN)
                .build();
    }
    public static ForgotPasswordRequest validForgotPasswordRequest() {
        return ForgotPasswordRequest.builder()
                .email(TestConstants.EMAIL)
                .build();
    }
    public static ConfirmForgotPasswordRequest validConfirmForgotPasswordRequest() {
        return ConfirmForgotPasswordRequest.builder()
                .email(TestConstants.EMAIL)
                .confirmationCode(TestConstants.CONFIRMATION_CODE)
                .newPassword(TestConstants.NEW_PASSWORD)
                .build();
    }
    public static UserProfileResponse validUserProfileResponse() {
        return UserProfileResponse.builder()
                .email(TestConstants.EMAIL)
                .name(TestConstants.NAME)
                .emailVerified(true)
                .userStatus("CONFIRMED")
                .createdAt(Instant.now())
                .lastModifiedAt(Instant.now())
                .build();
    }
    private TestMocks() {}
} 