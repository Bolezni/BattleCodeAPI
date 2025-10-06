package com.bolezni.dto;

import java.util.Set;

public record LoginResponse(
        String id,
        String username,
        String email,
        String token,
        Set<String> roles
) {
}
