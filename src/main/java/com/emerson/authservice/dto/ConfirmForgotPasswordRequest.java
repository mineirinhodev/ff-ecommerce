package com.emerson.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmForgotPasswordRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String confirmationCode;

    @NotBlank
    private String newPassword;
}