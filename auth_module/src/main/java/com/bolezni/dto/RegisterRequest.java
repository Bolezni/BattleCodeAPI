package com.bolezni.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 5, max = 50) String password,
        @NotBlank @Size(min = 5, max = 50) String passwordConfirmation,
        @NotNull String avatarUrl,
        @NotNull Set<String> roles,
        @NotNull String bio
) {
}
