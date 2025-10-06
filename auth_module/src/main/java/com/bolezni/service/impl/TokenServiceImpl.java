package com.bolezni.service.impl;

import com.bolezni.dto.TokenPair;
import com.bolezni.security.CustomUserDetails;
import com.bolezni.security.jwt.JwtProvider;
import com.bolezni.service.RedisService;
import com.bolezni.service.TokenService;
import com.bolezni.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TokenServiceImpl implements TokenService {

    JwtProvider jwtProvider;
    RedisService redisService;

    @Override
    public TokenPair generateTokenPair(CustomUserDetails userDetails, HttpServletResponse response) {
        String jwtToken = jwtProvider.buildJwtToken(userDetails);
        String refreshToken = jwtProvider.buildRefreshJwtToken(userDetails);

        CompletableFuture.runAsync(() -> {
            try {
                redisService.save(userDetails.user().getId(), refreshToken, 7, TimeUnit.DAYS);
                log.info("Refresh token generated successfully for {}", userDetails.user().getId());
            } catch (Exception e) {
                log.error("Refresh token generation failed", e);
            }
        }).exceptionally(e -> {
            log.error("Refresh token generation failed", e);
            return null;
        });

        CookieUtils.createCookie(response, "refresh_token", refreshToken);

        return new TokenPair(jwtToken);
    }

    @Override
    public boolean validatedAccessToken(String accessToken, UserDetails userDetails) {
        try {
            return jwtProvider.isValidToken(accessToken, userDetails);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public String extractUsername(String accessToken) {
        return jwtProvider.extractUsername(accessToken);
    }

    @Override
    public void revokeRefreshToken(String key) {
        redisService.delete(key);
    }
}
