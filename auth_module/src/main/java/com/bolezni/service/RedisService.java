package com.bolezni.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    void save(String key, String value, int ttl, TimeUnit timeUnit);

    Optional<String> get(String key);

    void delete(String key);
}
