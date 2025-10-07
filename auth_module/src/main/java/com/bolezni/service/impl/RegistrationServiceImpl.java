package com.bolezni.service.impl;

import com.bolezni.dto.RegisterRequest;
import com.bolezni.event.UserRegisteredEvent;
import com.bolezni.service.RedisService;
import com.bolezni.service.RegistrationService;
import com.bolezni.store.entity.Roles;
import com.bolezni.store.entity.UserEntity;
import com.bolezni.store.repository.UserRepository;
import com.bolezni.utils.VerificationCodeGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegistrationServiceImpl implements RegistrationService {
    ApplicationEventPublisher eventPublisher;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RedisService redisService;

    @Override
    @Transactional
    public void register(RegisterRequest request) throws ServiceUnavailableException {
        validateRegisterRequest(request);

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(processRoles(request.roles()))
                .bio(request.bio())
                .avatarUrl(request.avatarUrl())
                .build();

        String verificationCode = VerificationCodeGenerator.generate();

        try {
            redisService.save(user.getEmail(), verificationCode, 1, TimeUnit.DAYS);
            log.info("Verification code saved to Redis for user {}", user.getEmail());
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failed for user {}", user.getEmail(), e);
            throw new ServiceUnavailableException("Email verification service is temporarily unavailable. Please try again later.");
        }

        userRepository.save(user);

        eventPublisher.publishEvent(new UserRegisteredEvent(
                this,
                user.getEmail(),
                verificationCode
        ));
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            log.error("Request is null");
            throw new IllegalArgumentException("request cannot be null");
        }

        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            log.error("Username or email already exists");
            throw new IllegalArgumentException("Username or email already exists");
        }

        if (passwordEncoder.matches(request.password(), request.passwordConfirmation())) {
            log.error("Passwords do not match");
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    private Set<Roles> processRoles(Set<String> roleStrings) {
        return roleStrings != null ? roleStrings.stream()
                .map(String::toUpperCase)
                .map(Roles::valueOf)
                .collect(Collectors.toSet()) : Set.of(Roles.DEVELOPER);
    }
}
