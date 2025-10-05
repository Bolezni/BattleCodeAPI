package com.bolezni.service;

import java.util.Optional;

public interface RefreshTokenService {

    void save(String userId, String refreshToken);

    Optional<String> get(String userId);

    void delete(String userId);

    boolean exists(String userId);
}
