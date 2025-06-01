package com.emerson.authservice.constants;

public class TestConstants {
    // AWS Cognito Constants
    public static final String CLIENT_ID = "test-client-id";
    public static final String USER_POOL_ID = "test-pool-id";
    
    // User Data Constants
    public static final String TEST_EMAIL = "test@example.com";
    public static final String TEST_PASSWORD = "password123";
    public static final String TEST_NAME = "Test User";
    public static final String TEST_CODE = "123456";
    public static final String TEST_NEW_PASSWORD = "newpassword123";
    
    // Token Constants
    public static final String ACCESS_TOKEN = "access-token";
    public static final String NEW_ACCESS_TOKEN = "new-access-token";
    public static final String ID_TOKEN = "id-token";
    public static final String NEW_ID_TOKEN = "new-id-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String NEW_REFRESH_TOKEN = "new-refresh-token";
    public static final Integer TOKEN_EXPIRES_IN = 3600;
    
    // Error Messages
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    public static final String USER_EXISTS_MESSAGE = "User exists";
    public static final String INVALID_CODE_MESSAGE = "Invalid verification code";
    
    // Response Messages
    public static final String CREDENTIALS_INVALID = "Invalid credentials";
    public static final String EMAIL_ALREADY_REGISTERED = "Email already registered";
    public static final String INVALID_CODE = "Invalid verification code";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    
    // API Endpoints
    public static final String AUTH_BASE_URL = "/api/auth";
    public static final String USER_BASE_URL = "/api/user";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REGISTER_ENDPOINT = "/register";
    public static final String FORGOT_PASSWORD_ENDPOINT = "/forgot-password";
    public static final String CONFIRM_FORGOT_PASSWORD_ENDPOINT = "/confirm-forgot-password";
    public static final String REFRESH_TOKEN_ENDPOINT = "/refresh-token";
    public static final String PROFILE_ENDPOINT = "/profile";

} 