package com.emerson.authservice.service;

import com.emerson.authservice.dto.UserProfileResponse;
import com.emerson.authservice.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.Instant;
import java.util.Arrays;

import static com.emerson.authservice.constants.TestConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CognitoUserServiceTest {

    @Mock
    private CognitoIdentityProviderClient cognitoClient;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private CognitoUserService userService;

    @BeforeEach
    void setUp() {
        String USER_POOL_ID = "test-pool-id";
        userService = new CognitoUserService(cognitoClient, USER_POOL_ID);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getCurrentUserProfile_Success() {
        // Arrange
        String email = "test@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(authentication.isAuthenticated()).thenReturn(true);

        Instant now = Instant.now();
        AdminGetUserResponse userResponse = AdminGetUserResponse.builder()
                .username(email)
                .userStatus(UserStatusType.CONFIRMED)
                .userCreateDate(now)
                .userLastModifiedDate(now)
                .userAttributes(Arrays.asList(
                    AttributeType.builder()
                        .name("name")
                        .value("Test User")
                        .build(),
                    AttributeType.builder()
                        .name("email")
                        .value(email)
                        .build(),
                    AttributeType.builder()
                        .name("email_verified")
                        .value("true")
                        .build()
                ))
                .build();

        when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class)))
                .thenReturn(userResponse);

        // Act
        UserProfileResponse profile = userService.getCurrentUserProfile();

        // Assert
        assertNotNull(profile);
        assertEquals(email, profile.getEmail());
        assertEquals("Test User", profile.getName());
        assertTrue(profile.isEmailVerified());
        assertEquals("CONFIRMED", profile.getUserStatus());
        assertNotNull(profile.getCreatedAt());
        assertNotNull(profile.getLastModifiedAt());

        verify(cognitoClient).adminGetUser(any(AdminGetUserRequest.class));
    }

    @Test
    void getCurrentUserProfile_UserNotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                userService.getCurrentUserProfile());

        assertEquals("User not authenticated", exception.getMessage());
        verify(cognitoClient, never()).adminGetUser(any(AdminGetUserRequest.class));
    }

    @Test
    void getCurrentUserProfile_NoAuthentication() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                userService.getCurrentUserProfile());

        assertEquals("User not authenticated", exception.getMessage());
        verify(cognitoClient, never()).adminGetUser(any(AdminGetUserRequest.class));
    }

    @Test
    void getCurrentUserProfile_UserNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class)))
                .thenThrow(UserNotFoundException.builder().message(USER_NOT_FOUND).build());

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                userService.getCurrentUserProfile());

        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }
} 