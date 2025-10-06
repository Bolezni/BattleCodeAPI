package com.bolezni.dto;

import com.bolezni.security.CustomUserDetails;

public record AuthenticationResult(CustomUserDetails userDetails) {
}
