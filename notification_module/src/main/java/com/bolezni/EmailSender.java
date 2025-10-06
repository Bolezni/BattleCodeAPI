package com.bolezni;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);
}
