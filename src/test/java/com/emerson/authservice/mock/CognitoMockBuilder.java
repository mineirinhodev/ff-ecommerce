package com.emerson.authservice.mock;

import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import static com.emerson.authservice.constants.TestConstants.*;

public class CognitoMockBuilder {

    public static AuthenticationResultType buildAuthenticationResult(boolean isRefresh) {
        return AuthenticationResultType.builder()
                .accessToken(isRefresh ? NEW_ACCESS_TOKEN : ACCESS_TOKEN)
                .idToken(isRefresh ? NEW_ID_TOKEN : ID_TOKEN)
                .refreshToken(isRefresh ? NEW_REFRESH_TOKEN : REFRESH_TOKEN)
                .expiresIn(TOKEN_EXPIRES_IN)
                .build();
    }

    public static InitiateAuthResponse buildInitiateAuthResponse(boolean isRefresh) {
        return InitiateAuthResponse.builder()
                .authenticationResult(buildAuthenticationResult(isRefresh))
                .build();
    }

    public static AdminCreateUserResponse buildAdminCreateUserResponse() {
        return AdminCreateUserResponse.builder()
                .user(UserType.builder().build())
                .build();
    }

    public static AdminSetUserPasswordResponse buildAdminSetUserPasswordResponse() {
        return AdminSetUserPasswordResponse.builder().build();
    }

    public static ForgotPasswordResponse buildForgotPasswordResponse() {
        return ForgotPasswordResponse.builder().build();
    }

    public static ConfirmForgotPasswordResponse buildConfirmForgotPasswordResponse() {
        return ConfirmForgotPasswordResponse.builder().build();
    }

    public static NotAuthorizedException buildNotAuthorizedException() {
        return NotAuthorizedException.builder()
                .message(INVALID_CREDENTIALS_MESSAGE)
                .build();
    }

    public static UsernameExistsException buildUsernameExistsException() {
        return UsernameExistsException.builder()
                .message(USER_EXISTS_MESSAGE)
                .build();
    }

    public static CodeMismatchException buildCodeMismatchException() {
        return CodeMismatchException.builder()
                .message(INVALID_CODE_MESSAGE)
                .build();
    }
} 