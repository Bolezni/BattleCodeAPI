package com.bolezni.service.impl;

import com.bolezni.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String userId, String refreshToken) {
        log.info("Saving refresh token for user: {}", userId);
        redisTemplate.opsForValue().set(userId, refreshToken, 7, TimeUnit.DAYS);
    }

    @Override
    public Optional<String> get(String userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(userId));
    }

    @Override
    public void delete(String userId) {
        redisTemplate.delete(userId);
    }

    @Override
    public boolean exists(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(userId));
    }
}
