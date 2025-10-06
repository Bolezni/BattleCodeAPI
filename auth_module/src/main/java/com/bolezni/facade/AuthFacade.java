package com.bolezni.facade;

import com.bolezni.dto.*;
import com.bolezni.security.CustomUserDetails;
import com.bolezni.service.AuthService;
import com.bolezni.service.RefreshTokenValidationService;
import com.bolezni.service.RegistrationService;
import com.bolezni.service.TokenService;
import com.bolezni.store.entity.UserEntity;
import com.bolezni.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthFacade {
    AuthService authenticationService;
    TokenService tokenService;
    RegistrationService userRegistrationService;
    UserDetailsService userDetailsService;
    RefreshTokenValidationService refreshTokenValidationService;

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        log.debug("Attempting login for user: {}", loginRequest.username());

        validateLoginRequest(loginRequest);

        AuthenticationResult authResult = authenticationService.authenticate(
                loginRequest.username(),
                loginRequest.password()
        );

        TokenPair tokenPair = tokenService.generateTokenPair(authResult.userDetails(), response);

        UserEntity user = authResult.userDetails().user();

        log.info("User {} successfully logged in", user.getUsername());

        return buildLoginResponse(user, tokenPair);
    }

    public void register(RegisterRequest request) {
        log.debug("Attempting registration for user: {}", request.username());

        userRegistrationService.register(request);

        log.info("User {} successfully registered", request.username());
    }

    public LoginResponse refreshToken(String refreshToken, HttpServletResponse response) {
        log.debug("Attempting token refresh");

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be empty");
        }

        UserEntity user = refreshTokenValidationService.validateAndGetUser(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getUsername());

        TokenPair newTokenPair = tokenService.generateTokenPair(userDetails, response);

        log.info("Tokens successfully refreshed for user: {}", user.getUsername());

        return buildLoginResponse(user, newTokenPair);
    }

    public void logout(String refreshToken, String userId, HttpServletResponse response) {
        log.debug("Attempting logout for user ID: {}", userId);

        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            tokenService.revokeRefreshToken(userId);
        }

        CookieUtils.deleteCookie(response, "refresh_token");

        log.info("User ID: {} successfully logged out", userId);
    }

    private void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }
        if (loginRequest.username() == null || loginRequest.username().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (loginRequest.password() == null || loginRequest.password().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    private LoginResponse buildLoginResponse(UserEntity user, TokenPair tokenPair) {
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                tokenPair.jwtToken(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );
    }
}
