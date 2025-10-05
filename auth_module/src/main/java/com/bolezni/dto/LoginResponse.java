package com.bolezni.dto;

public record LoginResponse(
        String id,
        String username,
        String email,
        String token,
        String refreshToken
) {
}
