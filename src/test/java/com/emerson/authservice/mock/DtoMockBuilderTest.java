package com.emerson.authservice.mock;

import com.emerson.authservice.dto.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.emerson.authservice.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class DtoMockBuilderTest {

    @Test
    void buildLoginRequest_Success() {
        LoginRequest request = DtoMockBuilder.buildLoginRequest();
        
        assertEquals(TEST_EMAIL, request.getEmail());
        assertEquals(TEST_PASSWORD, request.getPassword());
    }

    @Test
    void buildRegisterRequest_Success() {
        RegisterRequest request = DtoMockBuilder.buildRegisterRequest();
        
        assertEquals(TEST_EMAIL, request.getEmail());
        assertEquals(TEST_PASSWORD, request.getPassword());
        assertEquals(TEST_NAME, request.getName());
    }

    @Test
    void buildForgotPasswordRequest_Success() {
        ForgotPasswordRequest request = DtoMockBuilder.buildForgotPasswordRequest();
        
        assertEquals(TEST_EMAIL, request.getEmail());
    }

    @Test
    void buildConfirmForgotPasswordRequest_Success() {
        ConfirmForgotPasswordRequest request = DtoMockBuilder.buildConfirmForgotPasswordRequest();
        
        assertEquals(TEST_EMAIL, request.getEmail());
        assertEquals(TEST_CODE, request.getConfirmationCode());
        assertEquals(TEST_NEW_PASSWORD, request.getNewPassword());
    }

    @Test
    void buildRefreshTokenRequest_Success() {
        RefreshTokenRequest request = DtoMockBuilder.buildRefreshTokenRequest();
        
        assertEquals(REFRESH_TOKEN, request.getRefreshToken());
    }

    @Test
    void buildAuthenticationResponse_StandardFlow() {
        Map<String, String> response = DtoMockBuilder.buildAuthenticationResponse(false);
        
        assertEquals(ACCESS_TOKEN, response.get("accessToken"));
        assertEquals(ID_TOKEN, response.get("idToken"));
        assertEquals(REFRESH_TOKEN, response.get("refreshToken"));
        assertEquals(String.valueOf(TOKEN_EXPIRES_IN), response.get("expiresIn"));
    }

    @Test
    void buildAuthenticationResponse_RefreshFlow() {
        Map<String, String> response = DtoMockBuilder.buildAuthenticationResponse(true);
        
        assertEquals(NEW_ACCESS_TOKEN, response.get("accessToken"));
        assertEquals(NEW_ID_TOKEN, response.get("idToken"));
        assertEquals(NEW_REFRESH_TOKEN, response.get("refreshToken"));
        assertEquals(String.valueOf(TOKEN_EXPIRES_IN), response.get("expiresIn"));
    }

    @Test
    void buildUserProfileResponse_Success() {
        UserProfileResponse response = DtoMockBuilder.buildUserProfileResponse();
        
        assertEquals(TEST_EMAIL, response.getEmail());
        assertEquals(TEST_NAME, response.getName());
        assertTrue(response.isEmailVerified());
        assertEquals("CONFIRMED", response.getUserStatus());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getLastModifiedAt());
    }
} 