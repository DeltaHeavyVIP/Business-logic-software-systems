package com.example.blps.message.consumer;

import com.example.blps.message.SpamMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JmsReceiver {
    private final EmailService emailService;

    @Autowired
    public JmsReceiver(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = "${spring.rabbitmq.rate-movie-queue}")
    public void receiveMessage(@Payload SpamMessage message) {
        log.info("Message {} received", message);
        emailService.sendEmail(message);
    }
}
