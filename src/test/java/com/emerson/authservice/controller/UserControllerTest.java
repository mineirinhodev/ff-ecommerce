package com.emerson.authservice.controller;

import com.emerson.authservice.config.SecurityTestConfig;
import com.emerson.authservice.dto.UserProfileResponse;
import com.emerson.authservice.exception.AuthenticationException;
import com.emerson.authservice.service.CognitoUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static com.emerson.authservice.constants.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityTestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Setter
    @Getter
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CognitoUserService userService;

    @Test
    void getProfile_Success() throws Exception {
        // Arrange
        Instant now = Instant.now();
        UserProfileResponse mockProfile = UserProfileResponse.builder()
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .emailVerified(true)
                .userStatus("CONFIRMED")
                .createdAt(now)
                .lastModifiedAt(now)
                .build();

        when(userService.getCurrentUserProfile()).thenReturn(mockProfile);

        // Act & Assert
        mockMvc.perform(get(USER_BASE_URL + PROFILE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.emailVerified").value(true))
                .andExpect(jsonPath("$.userStatus").value("CONFIRMED"));

        verify(userService).getCurrentUserProfile();
    }

    @Test
    void getProfile_Unauthorized() throws Exception {
        // Arrange
        when(userService.getCurrentUserProfile())
                .thenThrow(new AuthenticationException(USER_NOT_AUTHENTICATED));

        // Act & Assert
        mockMvc.perform(get(USER_BASE_URL + PROFILE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(USER_NOT_AUTHENTICATED));

        verify(userService).getCurrentUserProfile();
    }

}