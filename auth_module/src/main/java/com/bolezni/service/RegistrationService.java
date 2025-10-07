package com.bolezni.service;

import com.bolezni.dto.RegisterRequest;

import javax.naming.ServiceUnavailableException;

public interface RegistrationService {
    void register(RegisterRequest registerRequest) throws ServiceUnavailableException;
}
