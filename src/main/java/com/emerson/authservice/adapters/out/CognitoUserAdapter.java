package com.emerson.authservice.adapters.out;

import com.emerson.authservice.domain.UserService;
import com.emerson.authservice.dto.UserProfileResponse;
import com.emerson.authservice.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Component
public class CognitoUserAdapter implements UserService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;

    public CognitoUserAdapter(
            CognitoIdentityProviderClient cognitoClient,
            @Value("${aws.cognito.user-pool-id}") String userPoolId) {
        this.cognitoClient = cognitoClient;
        this.userPoolId = userPoolId;
    }

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User not authenticated");
        }
        String email = null;
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            var claims = jwtAuth.getToken().getClaims();
            if (claims.get("email") != null) {
                email = claims.get("email").toString();
            } else if (claims.get("username") != null) {
                email = claims.get("username").toString();
            } else if (claims.get("sub") != null) {
                email = claims.get("sub").toString();
            }
        }
        if (email == null) {
            email = authentication.getName();
        }
        if (email == null) {
            throw new AuthenticationException("Email/username claim not found in token");
        }
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