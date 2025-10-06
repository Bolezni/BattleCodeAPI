package com.bolezni.service;

import com.bolezni.dto.AuthenticationResult;

public interface AuthService {

    AuthenticationResult authenticate(String username, String password);
}
