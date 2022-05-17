package com.example.blps.message.producer;

import com.example.blps.message.SpamMessage;
import com.example.blps.model.Users;
import com.example.blps.repositories.UsersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MessageService {
    private final RabbitMQSender rabbitMQSender;
    private final UsersRepo usersRepo;

    @Autowired
    public MessageService(RabbitMQSender rabbitMQSender, UsersRepo usersRepo) {
        this.rabbitMQSender = rabbitMQSender;
        this.usersRepo = usersRepo;
    }

    @Scheduled(fixedRate = 120000)
    @Async
    public void sendSpamMessage() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        log.info("Start send spam messages! Time: " + formatter.format(new Date()));
        List<Users> users = usersRepo.findAll();
        for (Users user : users) {
            String email = user.getEmail();
            if (email != null) {
                rabbitMQSender.send(new SpamMessage(email));
            }
        }
        log.info("End send spam messages! Time: " + formatter.format(new Date()));
    }
}
