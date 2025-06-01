package com.emerson.authservice.controller;

import com.emerson.authservice.config.SecurityTestConfig;
import com.emerson.authservice.dto.*;
import com.emerson.authservice.exception.AuthenticationException;
import com.emerson.authservice.exception.UserAlreadyExistsException;
import com.emerson.authservice.mock.DtoMockBuilder;
import com.emerson.authservice.service.CognitoAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.emerson.authservice.constants.TestConstants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityTestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CognitoAuthService authService;

    @Test
    void login_Success() throws Exception {
        // Arrange
        LoginRequest request = DtoMockBuilder.buildLoginRequest();
        when(authService.login(anyString(), anyString()))
                .thenReturn(DtoMockBuilder.buildAuthenticationResponse(false));

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.idToken").value(ID_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.expiresIn").value(String.valueOf(TOKEN_EXPIRES_IN)));

        verify(authService).login(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void login_InvalidCredentials() throws Exception {
        // Arrange
        LoginRequest request = DtoMockBuilder.buildLoginRequest();
        when(authService.login(anyString(), anyString()))
                .thenThrow(new AuthenticationException(CREDENTIALS_INVALID));

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(CREDENTIALS_INVALID));

        verify(authService).login(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void register_Success() throws Exception {
        // Arrange
        RegisterRequest request = DtoMockBuilder.buildRegisterRequest();
        doNothing().when(authService).register(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(authService).register(TEST_EMAIL, TEST_PASSWORD, TEST_NAME);
    }

    @Test
    void register_UserExists() throws Exception {
        // Arrange
        RegisterRequest request = DtoMockBuilder.buildRegisterRequest();
        doThrow(new UserAlreadyExistsException(EMAIL_ALREADY_REGISTERED))
                .when(authService).register(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(EMAIL_ALREADY_REGISTERED));

        verify(authService).register(TEST_EMAIL, TEST_PASSWORD, TEST_NAME);
    }

    @Test
    void forgotPassword_Success() throws Exception {
        // Arrange
        ForgotPasswordRequest request = DtoMockBuilder.buildForgotPasswordRequest();
        doNothing().when(authService).forgotPassword(anyString());

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + FORGOT_PASSWORD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService).forgotPassword(TEST_EMAIL);
    }

    @Test
    void confirmForgotPassword_Success() throws Exception {
        // Arrange
        ConfirmForgotPasswordRequest request = DtoMockBuilder.buildConfirmForgotPasswordRequest();
        doNothing().when(authService).confirmForgotPassword(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + CONFIRM_FORGOT_PASSWORD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService).confirmForgotPassword(TEST_EMAIL, TEST_CODE, TEST_NEW_PASSWORD);
    }

    @Test
    void confirmForgotPassword_InvalidCode() throws Exception {
        // Arrange
        ConfirmForgotPasswordRequest request = DtoMockBuilder.buildConfirmForgotPasswordRequest();
        doThrow(new AuthenticationException(INVALID_CODE))
                .when(authService).confirmForgotPassword(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + CONFIRM_FORGOT_PASSWORD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(INVALID_CODE));

        verify(authService).confirmForgotPassword(TEST_EMAIL, TEST_CODE, TEST_NEW_PASSWORD);
    }

    @Test
    void refreshToken_Success() throws Exception {
        // Arrange
        RefreshTokenRequest request = DtoMockBuilder.buildRefreshTokenRequest();
        when(authService.refreshToken(anyString()))
                .thenReturn(DtoMockBuilder.buildAuthenticationResponse(true));

        // Act & Assert
        mockMvc.perform(post(AUTH_BASE_URL + REFRESH_TOKEN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(NEW_ACCESS_TOKEN))
                .andExpect(jsonPath("$.idToken").value(NEW_ID_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(NEW_REFRESH_TOKEN))
                .andExpect(jsonPath("$.expiresIn").value(String.valueOf(TOKEN_EXPIRES_IN)));

        verify(authService).refreshToken(REFRESH_TOKEN);
    }
} 