package com.emerson.authservice.adapters.out;

import com.emerson.authservice.adapters.out.jwt.JwtService;
import com.emerson.authservice.adapters.out.persistence.UserEntity;
import com.emerson.authservice.adapters.out.persistence.UserRepository;
import com.emerson.authservice.domain.UserService;
import com.emerson.authservice.dto.UserProfileResponse;
import com.emerson.authservice.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalUserAdapter implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User not authenticated");
        }

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        return UserProfileResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .emailVerified(user.isEmailVerified())
                .userStatus(user.getStatus())
                .createdAt(user.getCreatedAt())
                .lastModifiedAt(user.getLastModifiedAt())
                .build();
    }
} 