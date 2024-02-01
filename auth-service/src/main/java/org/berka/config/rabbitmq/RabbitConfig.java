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

    @Value("${rabbitmq.authExchange}")
    private String authExchange;

    @Value("${rabbitmq.register-user-queue}")
    private String registerUserQueue;
    @Value("${rabbitmq.register-user-bindingKey}")
    private String registerUserBindingKey;

    @Value("${rabbitmq.delete-user-queue}")
    private String deleteUserQueue;

    @Bean
    DirectExchange exchangeAuth(){
        return new DirectExchange(authExchange);
    }


    @Bean
    Queue deleteUserQueue(){
        return new Queue(deleteUserQueue);
    }

    @Bean
    Queue registerUserQueue(){
        return new Queue(registerUserQueue);
    }

    @Bean
    public Binding registerUserBinding(final DirectExchange authExchange, final Queue registerUserQueue){
        return BindingBuilder.bind(registerUserQueue).to(authExchange).with(registerUserBindingKey);
    }
}
