package com.emerson.authservice.mocks;

public final class TestConstants {
    public static final String EMAIL = "a@b.com";
    public static final String INVALID_EMAIL = "invalid";
    public static final String PASSWORD = "12345678";
    public static final String SHORT_PASSWORD = "123";
    public static final String NAME = "User";
    public static final String EMPTY = "";
    public static final String REFRESH_TOKEN = "refresh";
    public static final String INVALID_REFRESH_TOKEN = "invalid";
    public static final String ACCESS_TOKEN = "token";
    public static final String CONFIRMATION_CODE = "1234";
    public static final String NEW_PASSWORD = "novaSenha123";
    public static final String INVALID_CONFIRMATION_CODE = "0000";

    // Mensagens de exceção
    public static final String INVALID_CREDENTIALS_MSG = "Invalid credentials";
    public static final String USER_ALREADY_EXISTS_MSG = "Email already registered";
    public static final String INVALID_REFRESH_TOKEN_MSG = "Invalid refresh token";
    public static final String USER_NOT_AUTHENTICATED_MSG = "User not authenticated";

    // Nomes de campos
    public static final String EMAIL_FIELD = "email";
    public static final String PASSWORD_FIELD = "password";
    public static final String NAME_FIELD = "name";
    public static final String ACCESS_TOKEN_FIELD = "accessToken";

    private TestConstants() {}
} 