package org.berka.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.userExchange}")
    private String exchangeUser;

    @Value("${rabbitmq.register-user-queue}")
    private String registerUserQueue;

    @Value("${rabbitmq.delete-user-queue}")
    private String deleteUserQueue;
    @Value("${rabbitmq.delete-user-bindingKey}")
    private String deleteUserBindingKey;


    @Bean
    DirectExchange exchangeUser(){
        return new DirectExchange(exchangeUser);
    }

    @Bean
    Queue deleteUserQueue(){
        return new Queue(deleteUserQueue);
    }

    @Bean
    public Binding deleteUserBinding(final DirectExchange exchangeUser,final Queue deleteUserQueue){
        return BindingBuilder.bind(deleteUserQueue).to(exchangeUser).with(deleteUserBindingKey);
    }

    @Bean
    Queue registerUserQueue(){
        return new Queue(registerUserQueue);
    }
}
