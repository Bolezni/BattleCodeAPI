package com.bolezni.event;

import lombok.Getter;

@Getter
public class UserRegisteredEvent extends BaseEvent<String> {
    private final String verificationToken;

    public UserRegisteredEvent(Object source, String data,String verificationToken) {
        super(source, data);
        this.verificationToken = verificationToken;
    }
}
