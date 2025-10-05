package com.bolezni.service.impl;

import com.bolezni.dto.LoginRequest;
import com.bolezni.dto.LoginResponse;
import com.bolezni.dto.RegisterRequest;
import com.bolezni.security.CustomUserDetails;
import com.bolezni.security.jwt.JwtProvider;
import com.bolezni.service.AuthService;
import com.bolezni.store.entity.Roles;
import com.bolezni.store.entity.UserEntity;
import com.bolezni.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;
    UserDetailsService userDetailsService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("loginRequest cannot be null");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        ));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            log.error("UserDetails is null");
            throw new IllegalArgumentException("userDetails cannot be null");
        }

        String jwtToken = jwtProvider.buildJwtToken(userDetails);
        String refreshToken = jwtProvider.buildRefreshJwtToken(userDetails);

        UserEntity user = userDetails.user();

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                jwtToken,
                refreshToken
        );
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (request == null) {
            log.error("Request is null");
            throw new IllegalArgumentException("request cannot be null");
        }

        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            log.error("Username or email already exists");
            throw new IllegalArgumentException("Username or email already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(processRoles(request.roles()))
                .bio(request.bio())
                .avatarUrl(request.avatarUrl())
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        if(refreshToken.isEmpty()){
            log.error("Refresh token is empty");
            throw new IllegalArgumentException("refreshToken cannot be empty");
        }

        String username = jwtProvider.extractUsername(refreshToken);

        assert username.isBlank();

        CustomUserDetails userDetails;
        try{
            userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        }catch (Exception e){
            log.warn("Couldn't upload user data for username: {}", username);
            throw new IllegalArgumentException("Invalid refresh token: user not found");
        }

        if (!jwtProvider.isValidToken(refreshToken, userDetails)) {
            log.warn("Invalid refresh token for user: {}", username);
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        String newJwtToken = jwtProvider.buildJwtToken(userDetails);
        String newRefreshToken = jwtProvider.buildRefreshJwtToken(userDetails);

        return new LoginResponse(
                userDetails.user().getId(),
                userDetails.user().getUsername(),
                userDetails.user().getEmail(),
                newJwtToken,
                newRefreshToken
        );
    }

    private Set<Roles> processRoles(Set<String> roleStrings) {
        return roleStrings != null ? roleStrings.stream()
                .map(String::toUpperCase)
                .map(Roles::valueOf)
                .collect(Collectors.toSet()) : Set.of(Roles.DEVELOPER);
    }
}
