package com.example.blps.config;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;

    @Bean
    public ConnectionFactory jmsConnectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate defaultJmsTemplate(@Autowired ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(@Autowired ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver((session, destinationName, pubSubDomain) -> {
            RMQDestination jmsDestination = new RMQDestination();
            jmsDestination.setDestinationName(destinationName);
            jmsDestination.setAmqpQueueName(destinationName);
            jmsDestination.setAmqp(true);
            return jmsDestination;
        });
        factory.setAutoStartup(true);
        return factory;
    }
}
