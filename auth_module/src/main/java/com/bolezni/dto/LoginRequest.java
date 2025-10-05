package com.bolezni.dto;

public record LoginRequest(
        String username,
        String password
) {
}
