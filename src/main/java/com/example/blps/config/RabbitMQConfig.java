package com.example.blps.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.rate-movie-queue}")
    private String spamQueueName;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.rate-movie-routingKey}")
    private String spamRoutingKey;

    @Bean
    DirectExchange appExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Queue spamQueue() {
        return new Queue(spamQueueName);
    }

    @Bean
    Binding declareBindingAddReview() {
        return BindingBuilder.bind(spamQueue()).to(appExchange()).with(spamRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}