package com.emerson.authservice.adapters.out;

import com.emerson.authservice.domain.AuthService;
import com.emerson.authservice.exception.AuthenticationException;
import com.emerson.authservice.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CognitoAuthAdapter implements AuthService {

    private final CognitoIdentityProviderClient cognitoClient;
    @Value("${aws.cognito.client-id}") String clientId;
    @Value("${aws.cognito.user-pool-id}") String userPoolId;

    @Override
    public Map<String, String> login(String email, String password) {
        try {
            Map<String, String> authParams = new HashMap<>();
            authParams.put("USERNAME", email);
            authParams.put("PASSWORD", password);

            InitiateAuthRequest request = InitiateAuthRequest.builder()
                    .clientId(clientId)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .authParameters(authParams)
                    .build();

            InitiateAuthResponse response = cognitoClient.initiateAuth(request);
            AuthenticationResultType authResult = response.authenticationResult();

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", authResult.accessToken());
            tokens.put("idToken", authResult.idToken());
            tokens.put("refreshToken", authResult.refreshToken());
            tokens.put("expiresIn", String.valueOf(authResult.expiresIn()));

            return tokens;
        } catch (NotAuthorizedException e) {
            throw new AuthenticationException("Invalid credentials");
        } catch (Exception e) {
            throw new AuthenticationException("Error during login: " + e.getMessage());
        }
    }

    @Override
    public void register(String email, String password, String name) {
        try {
            AdminCreateUserRequest createRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .userAttributes(
                            AttributeType.builder().name("email").value(email).build(),
                            AttributeType.builder().name("name").value(name).build(),
                            AttributeType.builder().name("email_verified").value("true").build()
                    )
                    .messageAction(MessageActionType.SUPPRESS)
                    .build();

            cognitoClient.adminCreateUser(createRequest);

            AdminSetUserPasswordRequest passwordRequest = AdminSetUserPasswordRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .password(password)
                    .permanent(true)
                    .build();

            cognitoClient.adminSetUserPassword(passwordRequest);
        } catch (UsernameExistsException e) {
            throw new UserAlreadyExistsException("Email already registered");
        } catch (Exception e) {
            throw new AuthenticationException("Error during registration: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {
        return authenticate(
                Map.of("REFRESH_TOKEN", refreshToken)
        );
    }

    private Map<String, String> authenticate(Map<String, String> authParams) {
        try {
            InitiateAuthResponse response = cognitoClient.initiateAuth(InitiateAuthRequest.builder()
                    .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                    .clientId(clientId)
                    .authParameters(authParams)
                    .build());

            return extractTokens(response.authenticationResult());

        } catch (CognitoIdentityProviderException e) {
            throw translateCognitoException(e);
        }
    }

    private Map<String, String> extractTokens(AuthenticationResultType result) {
        Map<String, String> tokens = new HashMap<>();
        if (result.idToken() != null) tokens.put("idToken", result.idToken());
        if (result.accessToken() != null) tokens.put("accessToken", result.accessToken());
        if (result.refreshToken() != null) tokens.put("refreshToken", result.refreshToken());
        tokens.put("expiresIn", String.valueOf(result.expiresIn()));
        return tokens;
    }

    private RuntimeException translateCognitoException(CognitoIdentityProviderException e) {
        return switch (e) {
            case NotAuthorizedException ignored -> new RuntimeException("Invalid credentials");
            case UserNotFoundException ignored -> new RuntimeException("User not found");
            case UsernameExistsException ignored -> new RuntimeException("User already exists");
            case InvalidPasswordException ignored -> new RuntimeException("Password does not meet requirements");
            case InvalidParameterException ignored -> new RuntimeException("Invalid parameters");
            default -> new RuntimeException("Cognito Error: " + e.awsErrorDetails().errorMessage(), e);
        };
    }

    @Override
    public void forgotPassword(String email) {
        try {
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .clientId(clientId)
                    .username(email)
                    .build();

            cognitoClient.forgotPassword(request);
        } catch (Exception e) {
            throw new AuthenticationException("Error requesting password recovery: " + e.getMessage());
        }
    }

    @Override
    public void confirmForgotPassword(String email, String code, String newPassword) {
        try {
            ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
                    .clientId(clientId)
                    .username(email)
                    .confirmationCode(code)
                    .password(newPassword)
                    .build();

            cognitoClient.confirmForgotPassword(request);
        } catch (CodeMismatchException e) {
            throw new AuthenticationException("Invalid verification code");
        } catch (Exception e) {
            throw new AuthenticationException("Error confirming new password: " + e.getMessage());
        }
    }
} 