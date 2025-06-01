package com.emerson.authservice.service;

import com.emerson.authservice.exception.AuthenticationException;
import com.emerson.authservice.exception.UserAlreadyExistsException;
import com.emerson.authservice.mock.CognitoMockBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

import static com.emerson.authservice.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CognitoAuthServiceTest {

    @Mock
    private CognitoIdentityProviderClient cognitoClient;

    @InjectMocks
    private CognitoAuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "clientId", CLIENT_ID);
        ReflectionTestUtils.setField(authService, "userPoolId", USER_POOL_ID);
    }

    @Test
    void login_Success() {
        // Arrange
        when(cognitoClient.initiateAuth(any(InitiateAuthRequest.class)))
                .thenReturn(CognitoMockBuilder.buildInitiateAuthResponse(false));

        // Act
        Map<String, String> result = authService.login(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertNotNull(result);
        assertEquals(ACCESS_TOKEN, result.get("accessToken"));
        assertEquals(ID_TOKEN, result.get("idToken"));
        assertEquals(REFRESH_TOKEN, result.get("refreshToken"));
        assertEquals(String.valueOf(TOKEN_EXPIRES_IN), result.get("expiresIn"));

        verify(cognitoClient).initiateAuth(any(InitiateAuthRequest.class));
    }

    @Test
    void login_InvalidCredentials() {
        // Arrange
        when(cognitoClient.initiateAuth(any(InitiateAuthRequest.class)))
                .thenThrow(CognitoMockBuilder.buildNotAuthorizedException());

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                authService.login(TEST_EMAIL, TEST_PASSWORD));

        assertEquals(CREDENTIALS_INVALID, exception.getMessage());
        verify(cognitoClient).initiateAuth(any(InitiateAuthRequest.class));
    }

    @Test
    void register_Success() {
        // Arrange
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class)))
                .thenReturn(CognitoMockBuilder.buildAdminCreateUserResponse());
        
        when(cognitoClient.adminSetUserPassword(any(AdminSetUserPasswordRequest.class)))
                .thenReturn(CognitoMockBuilder.buildAdminSetUserPasswordResponse());

        // Act & Assert
        assertDoesNotThrow(() -> authService.register(TEST_EMAIL, TEST_PASSWORD, TEST_NAME));

        verify(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));
        verify(cognitoClient).adminSetUserPassword(any(AdminSetUserPasswordRequest.class));
    }

    @Test
    void register_UserExists() {
        // Arrange
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class)))
                .thenThrow(CognitoMockBuilder.buildUsernameExistsException());

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () ->
                authService.register(TEST_EMAIL, TEST_PASSWORD, TEST_NAME));

        assertEquals(EMAIL_ALREADY_REGISTERED, exception.getMessage());
        verify(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));
        verify(cognitoClient, never()).adminSetUserPassword(any(AdminSetUserPasswordRequest.class));
    }

    @Test
    void forgotPassword_Success() {
        // Arrange
        when(cognitoClient.forgotPassword(any(ForgotPasswordRequest.class)))
                .thenReturn(CognitoMockBuilder.buildForgotPasswordResponse());

        // Act & Assert
        assertDoesNotThrow(() -> authService.forgotPassword(TEST_EMAIL));

        verify(cognitoClient).forgotPassword(any(ForgotPasswordRequest.class));
    }

    @Test
    void confirmForgotPassword_Success() {
        // Arrange
        when(cognitoClient.confirmForgotPassword(any(ConfirmForgotPasswordRequest.class)))
                .thenReturn(CognitoMockBuilder.buildConfirmForgotPasswordResponse());

        // Act & Assert
        assertDoesNotThrow(() -> 
            authService.confirmForgotPassword(TEST_EMAIL, "123456", "newpassword123")
        );

        verify(cognitoClient).confirmForgotPassword(any(ConfirmForgotPasswordRequest.class));
    }

    @Test
    void confirmForgotPassword_InvalidCode() {
        // Arrange
        when(cognitoClient.confirmForgotPassword(any(ConfirmForgotPasswordRequest.class)))
                .thenThrow(CognitoMockBuilder.buildCodeMismatchException());

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                authService.confirmForgotPassword(TEST_EMAIL, "wrong-code", "newpassword123"));

        assertEquals(INVALID_CODE, exception.getMessage());
        verify(cognitoClient).confirmForgotPassword(any(ConfirmForgotPasswordRequest.class));
    }

    @Test
    void refreshToken_Success() {
        // Arrange
        when(cognitoClient.initiateAuth(any(InitiateAuthRequest.class)))
                .thenReturn(CognitoMockBuilder.buildInitiateAuthResponse(true));

        // Act
        Map<String, String> result = authService.refreshToken(REFRESH_TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals(NEW_ACCESS_TOKEN, result.get("accessToken"));
        assertEquals(NEW_ID_TOKEN, result.get("idToken"));
        assertEquals(NEW_REFRESH_TOKEN, result.get("refreshToken"));
        assertEquals(String.valueOf(TOKEN_EXPIRES_IN), result.get("expiresIn"));

        verify(cognitoClient).initiateAuth(any(InitiateAuthRequest.class));
    }
} 