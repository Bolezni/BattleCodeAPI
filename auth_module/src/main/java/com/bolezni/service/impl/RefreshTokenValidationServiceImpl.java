package com.bolezni.service.impl;

import com.bolezni.security.CustomUserDetails;
import com.bolezni.security.jwt.JwtProvider;
import com.bolezni.service.RedisService;
import com.bolezni.service.RefreshTokenValidationService;
import com.bolezni.store.entity.UserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RefreshTokenValidationServiceImpl implements RefreshTokenValidationService {
    UserDetailsService userDetailsService;
    RedisService redisService;
    JwtProvider jwtProvider;

    @Override
    public UserEntity validateAndGetUser(String refreshToken) {
        String username = jwtProvider.extractUsername(refreshToken);

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid refresh token: cannot extract username");
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        UserEntity user = userDetails.user();

        String storedRefreshToken = redisService.get(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (!jwtProvider.isValidToken(refreshToken, userDetails) || !storedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        return user;
    }
}
