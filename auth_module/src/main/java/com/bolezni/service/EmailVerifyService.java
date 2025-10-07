package com.bolezni.service;

public interface EmailVerifyService {
    void verifyEmail(String token, String email);

    void resendToken(String email);
}
