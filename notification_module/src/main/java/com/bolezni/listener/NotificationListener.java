package com.bolezni.listener;

import com.bolezni.email.EmailSender;
import com.bolezni.event.UserRegisteredEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NotificationListener {

    EmailSender emailSender;

    @Async("emailExecutor")
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisterEvent(UserRegisteredEvent event){
        log.info("Processing registration email for user: {}", event.getData());
        emailSender.sendEmail(event.getData(), "Код подтверждения",event.getVerificationToken());
    }
}
