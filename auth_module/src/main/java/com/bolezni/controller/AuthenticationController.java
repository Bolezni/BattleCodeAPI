package com.bolezni.controller;

import com.bolezni.dto.LoginRequest;
import com.bolezni.dto.LoginResponse;
import com.bolezni.dto.RegisterRequest;
import com.bolezni.service.AuthService;
import com.bolezni.store.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/auth")
public class AuthenticationController {
    AuthService authService;


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);

        return ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.toString())
                .data(loginResponse)
                .message("Success login")
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.toString())
                .message("Success register")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@NotNull @RequestBody Map<String,String> refreshRequest) {
        String refreshToken = refreshRequest.get("refreshToken");
        if(refreshToken != null) {
            LoginResponse loginResponse = authService.refreshToken(refreshToken);
            return ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.OK.toString())
                    .data(loginResponse)
                    .message("Success refresh")
                    .build();
        }

        return ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("Invalid refresh token")
                .build();
    }
}
