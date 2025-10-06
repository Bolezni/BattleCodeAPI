package com.bolezni.service;

import com.bolezni.dto.TokenPair;
import com.bolezni.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    TokenPair generateTokenPair(CustomUserDetails userDetails, HttpServletResponse response);

    boolean validatedAccessToken(String accessToken, UserDetails userDetails);

    String extractUsername(String accessToken);

    void revokeRefreshToken(String key);
}
