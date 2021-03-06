package com.example.blps.message.consumer;

import com.example.blps.message.SpamMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendEmail(SpamMessage spamMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(spamMessage.getEmail());
        message.setSubject("Incredible offer");
        message.setText("We have new products, click on the links and see.");
        javaMailSender.send(message);
    }
}
