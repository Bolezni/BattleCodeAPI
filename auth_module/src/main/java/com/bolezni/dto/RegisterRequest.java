package com.bolezni.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String passwordConfirmation,
        @NotNull String avatarUrl,
        @NotNull Set<String> roles,
        @NotNull String bio
) {
}
