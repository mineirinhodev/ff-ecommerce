package com.emerson.authservice.mock;

import com.emerson.authservice.dto.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.emerson.authservice.constants.TestConstants.*;

public class DtoMockBuilder {

    public static LoginRequest buildLoginRequest() {
        return LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
    }

    public static RegisterRequest buildRegisterRequest() {
        return RegisterRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .build();
    }

    public static ForgotPasswordRequest buildForgotPasswordRequest() {
        return ForgotPasswordRequest.builder()
                .email(TEST_EMAIL)
                .build();
    }

    public static ConfirmForgotPasswordRequest buildConfirmForgotPasswordRequest() {
        return ConfirmForgotPasswordRequest.builder()
                .email(TEST_EMAIL)
                .confirmationCode(TEST_CODE)
                .newPassword(TEST_NEW_PASSWORD)
                .build();
    }

    public static RefreshTokenRequest buildRefreshTokenRequest() {
        return RefreshTokenRequest.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();
    }

    public static Map<String, String> buildAuthenticationResponse(boolean isRefresh) {
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", isRefresh ? NEW_ACCESS_TOKEN : ACCESS_TOKEN);
        response.put("idToken", isRefresh ? NEW_ID_TOKEN : ID_TOKEN);
        response.put("refreshToken", isRefresh ? NEW_REFRESH_TOKEN : REFRESH_TOKEN);
        response.put("expiresIn", String.valueOf(TOKEN_EXPIRES_IN));
        return response;
    }

    public static UserProfileResponse buildUserProfileResponse() {
        Instant now = Instant.now();
        return UserProfileResponse.builder()
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .emailVerified(true)
                .userStatus("CONFIRMED")
                .createdAt(now)
                .lastModifiedAt(now)
                .build();
    }
} 