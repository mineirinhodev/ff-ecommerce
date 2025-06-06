package com.emerson.authservice.adapters.in;

import com.emerson.authservice.domain.UserService;
import com.emerson.authservice.mocks.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.emerson.authservice.mocks.TestMocks;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getProfile_ok() {
        var profile = TestMocks.validUserProfileResponse();
        when(userService.getCurrentUserProfile()).thenReturn(profile);
        var resp = userController.getProfile();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(profile, resp.getBody());
    }

    @Test
    void getProfile_unauthenticated() {
        when(userService.getCurrentUserProfile()).thenThrow(new com.emerson.authservice.exception.AuthenticationException(TestConstants.USER_NOT_AUTHENTICATED_MSG));
        Exception ex = assertThrows(com.emerson.authservice.exception.AuthenticationException.class, () -> userController.getProfile());
        assertEquals(TestConstants.USER_NOT_AUTHENTICATED_MSG, ex.getMessage());
    }
} 