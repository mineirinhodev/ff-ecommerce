package com.emerson.authservice.adapters.in;

import com.emerson.authservice.application.LoginUserUseCase;
import com.emerson.authservice.application.RegisterUserUseCase;
import com.emerson.authservice.application.ForgotPasswordUseCase;
import com.emerson.authservice.application.ConfirmForgotPasswordUseCase;
import com.emerson.authservice.application.RefreshTokenUseCase;
import com.emerson.authservice.dto.LoginRequest;
import com.emerson.authservice.dto.RegisterRequest;
import com.emerson.authservice.dto.ForgotPasswordRequest;
import com.emerson.authservice.dto.ConfirmForgotPasswordRequest;
import com.emerson.authservice.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final LoginUserUseCase loginUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ConfirmForgotPasswordUseCase confirmForgotPasswordUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Operation(summary = "User login", description = "Performs user login and returns access tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Parameter(description = "User login data")
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUserUseCase.execute(request.getEmail(), request.getPassword()));
    }

    @Operation(summary = "User registration", description = "Registers a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Parameter(description = "New user data")
            @Valid @RequestBody RegisterRequest request) {
        registerUserUseCase.execute(request.getEmail(), request.getPassword(), request.getName());
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Refresh token", description = "Updates the access token using the refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token updated successfully",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(
            @Parameter(description = "Refresh token")
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.execute(request.getRefreshToken()));
    }

    @Operation(summary = "Password recovery", description = "Initiates the password recovery process")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recovery code sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid email"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Parameter(description = "User email")
            @Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.execute(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Confirm new password", description = "Confirms the new password using the recovery code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "401", description = "Invalid verification code")
    })
    @PostMapping("/confirm-forgot-password")
    public ResponseEntity<Void> confirmForgotPassword(
            @Parameter(description = "Password confirmation data")
            @Valid @RequestBody ConfirmForgotPasswordRequest request) {
        confirmForgotPasswordUseCase.execute(request.getEmail(), request.getConfirmationCode(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
} 