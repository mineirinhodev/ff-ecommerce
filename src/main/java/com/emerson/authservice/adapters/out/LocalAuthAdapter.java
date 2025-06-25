package com.emerson.authservice.adapters.out;

import com.emerson.authservice.adapters.out.jwt.JwtService;
import com.emerson.authservice.adapters.out.persistence.RefreshTokenEntity;
import com.emerson.authservice.adapters.out.persistence.RefreshTokenRepository;
import com.emerson.authservice.adapters.out.persistence.UserEntity;
import com.emerson.authservice.adapters.out.persistence.UserRepository;
import com.emerson.authservice.domain.AuthService;
import com.emerson.authservice.exception.AuthenticationException;
import com.emerson.authservice.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalAuthAdapter implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Map<String, String> login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = generateAndSaveRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("expiresIn", String.valueOf(3600)); // 1 hour in seconds

        return tokens;
    }

    @Override
    @Transactional
    public void register(String email, String password, String name, String whatsappNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .whatsappNumber(whatsappNumber)
                .emailVerified(false)
                .status("ACTIVE")
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public Map<String, String> refreshToken(String refreshToken) {
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token"));

        if (tokenEntity.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new AuthenticationException("Refresh token expired");
        }

        UserEntity user = tokenEntity.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = generateAndSaveRefreshToken(user);

        // Delete old refresh token
        refreshTokenRepository.delete(tokenEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        tokens.put("expiresIn", String.valueOf(3600)); // 1 hour in seconds

        return tokens;
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));
        // TODO: Implement email sending with reset code
    }

    @Override
    @Transactional
    public void confirmForgotPassword(String email, String code, String newPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));
        // TODO: Implement code validation and password update
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private String generateAndSaveRefreshToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plusMillis(30 * 24 * 60 * 60 * 1000)) // 30 days
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }
} 