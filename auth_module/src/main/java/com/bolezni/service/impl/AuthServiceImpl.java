package com.bolezni.service.impl;

import com.bolezni.dto.AuthenticationResult;
import com.bolezni.security.CustomUserDetails;
import com.bolezni.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return new AuthenticationResult((CustomUserDetails) authentication.getPrincipal());
    }
}
