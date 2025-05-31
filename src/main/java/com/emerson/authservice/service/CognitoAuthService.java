package com.emerson.authservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.HashMap;
import java.util.Map;


@Service
public class CognitoAuthService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String clientId;
    private final String userPoolId;

    public CognitoAuthService(
            @Value("${aws.cognito.clientId}") String clientId,
            @Value("${aws.cognito.userPoolId}") String userPoolId,
            @Value("${aws.cognito.region}") String region
    ) {
        this.clientId = clientId;
        this.userPoolId = userPoolId;
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(region))
                .build();
    }


    public Map<String, String> login(String email, String password) {
        return authenticate(
                Map.of("USERNAME", email, "PASSWORD", password),
                AuthFlowType.USER_PASSWORD_AUTH
        );
    }


    public void register(String email, String password) {
        try {
            // 🔥 Cria o usuário no Cognito
            cognitoClient.signUp(SignUpRequest.builder()
                    .clientId(clientId)
                    .username(email)
                    .password(password)
                    .userAttributes(
                            AttributeType.builder()
                                    .name("email")
                                    .value(email)
                                    .build()
                    )
                    .build());

            cognitoClient.adminConfirmSignUp(AdminConfirmSignUpRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .build());

            cognitoClient.adminUpdateUserAttributes(AdminUpdateUserAttributesRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .userAttributes(
                            AttributeType.builder()
                                    .name("email_verified")
                                    .value("true")
                                    .build()
                    )
                    .build());

        } catch (UsernameExistsException e) {
            throw new RuntimeException("Usuário já existe.");
        } catch (InvalidPasswordException e) {
            throw new RuntimeException("Senha não atende aos requisitos de segurança.");
        } catch (InvalidParameterException e) {
            throw new RuntimeException("Parâmetros inválidos.");
        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Erro no Cognito: " + e.awsErrorDetails().errorMessage());
        }
    }

    /**
     * ✅ Refresh Token
     */
    public Map<String, String> refreshToken(String refreshToken) {
        return authenticate(
                Map.of("REFRESH_TOKEN", refreshToken),
                AuthFlowType.REFRESH_TOKEN_AUTH
        );
    }

    private Map<String, String> authenticate(Map<String, String> authParams, AuthFlowType flow) {
        try {
            InitiateAuthResponse response = cognitoClient.initiateAuth(InitiateAuthRequest.builder()
                    .authFlow(flow)
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
            case NotAuthorizedException ignored -> new RuntimeException("Credenciais inválidas.");
            case UserNotFoundException ignored -> new RuntimeException("Usuário não encontrado.");
            case UsernameExistsException ignored -> new RuntimeException("Usuário já existe.");
            case InvalidPasswordException ignored -> new RuntimeException("Senha não atende aos requisitos.");
            case InvalidParameterException ignored -> new RuntimeException("Parâmetros inválidos.");
            default -> new RuntimeException("Erro Cognito: " + e.awsErrorDetails().errorMessage(), e);
        };
    }

    public void forgotPassword(String email) {
        try {
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .clientId(clientId)
                    .username(email)
                    .build();

            cognitoClient.forgotPassword(request);

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Erro ao solicitar reset de senha: " + e.awsErrorDetails().errorMessage());
        }
    }

    public void confirmForgotPassword(String email, String confirmationCode, String newPassword) {
        try {
            ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
                    .clientId(clientId)
                    .username(email)
                    .confirmationCode(confirmationCode)
                    .password(newPassword)
                    .build();

            cognitoClient.confirmForgotPassword(request);

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Erro ao confirmar reset de senha: " + e.awsErrorDetails().errorMessage());
        }
    }
}