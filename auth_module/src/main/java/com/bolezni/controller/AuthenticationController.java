package com.bolezni.controller;

import com.bolezni.dto.LoginRequest;
import com.bolezni.dto.LoginResponse;
import com.bolezni.dto.RegisterRequest;
import com.bolezni.facade.AuthFacade;
import com.bolezni.store.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "auth_methods")
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/auth")
public class AuthenticationController {
    AuthFacade authFacade;


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authFacade.login(loginRequest, response);

        return ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.toString())
                .data(loginResponse)
                .message("Success login")
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authFacade.register(registerRequest);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.toString())
                .message("Success register")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            LoginResponse loginResponse = authFacade.refreshToken(refreshToken, response);
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

    @PostMapping("/logout/{id}")
    public ApiResponse<Void> logout(@PathVariable(name = "id") String id,
                                    @CookieValue(name = "refresh_token", required = false) String refreshToken,
                                    HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ApiResponse.<Void>builder()
                    .status(HttpStatus.BAD_REQUEST.name())
                    .message("Refresh token not found")
                    .build();
        }

        authFacade.logout(refreshToken, id, response);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.name())
                .message("Success logout")
                .build();
    }
}
