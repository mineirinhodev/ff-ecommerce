package com.emerson.authservice.mock;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import static com.emerson.authservice.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CognitoMockBuilderTest {

    @Test
    void buildAuthenticationResult_StandardFlow() {
        AuthenticationResultType result = CognitoMockBuilder.buildAuthenticationResult(false);
        
        assertEquals(ACCESS_TOKEN, result.accessToken());
        assertEquals(ID_TOKEN, result.idToken());
        assertEquals(REFRESH_TOKEN, result.refreshToken());
        assertEquals(TOKEN_EXPIRES_IN, result.expiresIn());
    }

    @Test
    void buildAuthenticationResult_RefreshFlow() {
        AuthenticationResultType result = CognitoMockBuilder.buildAuthenticationResult(true);
        
        assertEquals(NEW_ACCESS_TOKEN, result.accessToken());
        assertEquals(NEW_ID_TOKEN, result.idToken());
        assertEquals(NEW_REFRESH_TOKEN, result.refreshToken());
        assertEquals(TOKEN_EXPIRES_IN, result.expiresIn());
    }

    @Test
    void buildInitiateAuthResponse_ContainsAuthResult() {
        InitiateAuthResponse response = CognitoMockBuilder.buildInitiateAuthResponse(false);
        
        assertNotNull(response.authenticationResult());
        assertEquals(ACCESS_TOKEN, response.authenticationResult().accessToken());
    }

    @Test
    void buildAdminCreateUserResponse_ContainsUser() {
        AdminCreateUserResponse response = CognitoMockBuilder.buildAdminCreateUserResponse();
        
        assertNotNull(response.user());
    }

    @Test
    void buildNotAuthorizedException_HasCorrectMessage() {
        NotAuthorizedException exception = CognitoMockBuilder.buildNotAuthorizedException();
        
        assertEquals(INVALID_CREDENTIALS_MESSAGE, exception.getMessage());
    }

    @Test
    void buildUsernameExistsException_HasCorrectMessage() {
        UsernameExistsException exception = CognitoMockBuilder.buildUsernameExistsException();
        
        assertEquals(USER_EXISTS_MESSAGE, exception.getMessage());
    }

    @Test
    void buildCodeMismatchException_HasCorrectMessage() {
        CodeMismatchException exception = CognitoMockBuilder.buildCodeMismatchException();
        
        assertEquals(INVALID_CODE_MESSAGE, exception.getMessage());
    }
} 