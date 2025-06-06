package com.emerson.authservice.adapters.out;

import com.emerson.authservice.exception.AuthenticationException;
import com.emerson.authservice.mocks.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CognitoAuthAdapterTest {
    @Mock
    private CognitoIdentityProviderClient cognitoClient;

    @InjectMocks
    private CognitoAuthAdapter cognitoAuthAdapter;

    @BeforeEach
    void setUp() {

    }

    @Test
    void login_invalidCredentials_throwsAuthenticationException() {
        when(cognitoClient.initiateAuth(any(InitiateAuthRequest.class)))
            .thenThrow(NotAuthorizedException.builder().message("Not authorized").build());
        Exception ex = assertThrows(AuthenticationException.class, () ->
            cognitoAuthAdapter.login(TestConstants.EMAIL, TestConstants.PASSWORD)
        );
        assertEquals(TestConstants.INVALID_CREDENTIALS_MSG, ex.getMessage());
    }
} 