package com.emerson.authservice.domain;

import com.emerson.authservice.dto.UserProfileResponse;

public interface UserService {
    UserProfileResponse getCurrentUserProfile();
} 