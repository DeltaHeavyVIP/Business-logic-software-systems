package com.example.blps.message.rabbitmq;

import com.example.blps.message.model.SpamMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQSender {
    private final RabbitTemplate rabbitTemplate;
    private final Exchange appExchange;
    @Value("${rabbitmq.add-review-routingKey}")
    private String addReviewRoutingKey;

    public RabbitMQSender(RabbitTemplate rabbitTemplate, Exchange appExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.appExchange = appExchange;
    }

    public void send(SpamMessage message) {
        rabbitTemplate.convertAndSend(appExchange.getName(), addReviewRoutingKey, message);
        log.info("Message: {} sent", message);
    }
}
