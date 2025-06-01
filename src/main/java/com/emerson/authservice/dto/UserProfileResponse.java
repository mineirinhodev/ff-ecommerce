package com.emerson.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserProfileResponse {
    private String email;
    private String name;
    private boolean emailVerified;
    private String userStatus;
    private Instant createdAt;
    private Instant lastModifiedAt;
} 