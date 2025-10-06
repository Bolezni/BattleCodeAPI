package com.bolezni.service;

import com.bolezni.store.entity.UserEntity;

public interface RefreshTokenValidationService {
    UserEntity validateAndGetUser(String refreshToken);
}
