package com.emerson.authservice.service;

import com.emerson.authservice.dto.UserProfileResponse;
import com.emerson.authservice.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;

@Service
public class CognitoUserService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;

    public CognitoUserService(
            CognitoIdentityProviderClient cognitoClient,
            @Value("${aws.cognito.user-pool-id}") String userPoolId) {
        this.cognitoClient = cognitoClient;
        this.userPoolId = userPoolId;
    }

    public UserProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User not authenticated");
        }

        String email = authentication.getName();

        try {
            AdminGetUserRequest request = AdminGetUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .build();

            AdminGetUserResponse response = cognitoClient.adminGetUser(request);

            String name = response.userAttributes().stream()
                    .filter(attr -> attr.name().equals("name"))
                    .map(AttributeType::value)
                    .findFirst()
                    .orElse("");

            boolean emailVerified = response.userAttributes().stream()
                    .filter(attr -> attr.name().equals("email_verified"))
                    .map(attr -> Boolean.parseBoolean(attr.value()))
                    .findFirst()
                    .orElse(false);

            return UserProfileResponse.builder()
                    .email(email)
                    .name(name)
                    .emailVerified(emailVerified)
                    .userStatus(response.userStatus().toString())
                    .createdAt(response.userCreateDate())
                    .lastModifiedAt(response.userLastModifiedDate())
                    .build();

        } catch (UserNotFoundException e) {
            throw new AuthenticationException("User not found");
        } catch (Exception e) {
            throw new AuthenticationException("Error fetching user profile: " + e.getMessage());
        }
    }
} 