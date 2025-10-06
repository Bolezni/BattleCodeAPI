package com.bolezni.service.impl;

import com.bolezni.dto.RegisterRequest;
import com.bolezni.service.RegistrationService;
import com.bolezni.store.entity.Roles;
import com.bolezni.store.entity.UserEntity;
import com.bolezni.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegistrationServiceImpl implements RegistrationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        validateRegisterRequest(request);

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(processRoles(request.roles()))
                .bio(request.bio())
                .avatarUrl(request.avatarUrl())
                .build();

        userRepository.save(user);
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
