package com.bolezni.service.impl;

import com.bolezni.event.UserRegisteredEvent;
import com.bolezni.service.EmailVerifyService;
import com.bolezni.service.RedisService;
import com.bolezni.store.entity.UserEntity;
import com.bolezni.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailVerifyServiceImpl implements EmailVerifyService {
    ApplicationEventPublisher eventPublisher;
    UserRepository userRepository;
    RedisService redisService;

    @Override
    @Transactional
    public void verifyEmail(String token, String email) {
        validateInput(token, email);

        String tokenFromRedis = redisService.get(email)
                .orElseThrow(() -> new RuntimeException("Email token not found"));

        if (!token.equalsIgnoreCase(tokenFromRedis)) {
            log.error("token does not match");
            throw new IllegalArgumentException("token does not match");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        if (user.isVerified()) {
            redisService.delete(email);
            throw new IllegalArgumentException("Email is already verified");
        }

        user.setVerified(true);
        userRepository.save(user);

        redisService.delete(user.getEmail());
    }

    private void validateInput(String token, String email) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }

    @Override
    public void resendToken(String email) {
        if (email == null) {
            log.error("email is null");
            throw new IllegalArgumentException("email is null");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        if (user.isVerified()) {
            log.info("User already verified");
            throw new IllegalArgumentException("User already verified");
        }

        String token = redisService.get(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Token does not exist"));

        eventPublisher.publishEvent(new UserRegisteredEvent(
                this,
                user.getEmail(),
                token
        ));

    }
}
