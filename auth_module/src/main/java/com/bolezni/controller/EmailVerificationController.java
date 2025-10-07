package com.bolezni.controller;

import com.bolezni.service.EmailVerifyService;
import com.bolezni.store.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name="emails_methods")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailVerificationController {
    EmailVerifyService emailVerifyService;

    @GetMapping("/verify")
    public ApiResponse<Void> verifyEmail(@RequestParam(name = "token") @NotBlank String token,
                                         @RequestParam(name = "email") @NotBlank @Email String email) {
        emailVerifyService.verifyEmail(token, email);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.name())
                .message("Email verified")
                .build();
    }

    @PostMapping("/resend")
    public ApiResponse<Void> resendVerifyCode(@RequestParam(name = "email") @NotBlank @Email String email){
        emailVerifyService.resendToken(email);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.name())
                .message("The token has been sent")
                .build();
    }
}
