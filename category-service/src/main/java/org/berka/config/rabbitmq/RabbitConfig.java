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

    @Value("${rabbitmq.categoryExchange}")
    private String categoryExchange;

    @Value("${rabbitmq.category-id-queue}")
    private String categoryIdQueue;

    @Value("${rabbitmq.category-id-bindingKey}")
    private String categoryIdBindingKey;

    @Bean
    DirectExchange categoryExchange(){
        return new DirectExchange(categoryExchange);
    }

    @Bean
    Queue categoryIdQueue(){
        return new Queue(categoryIdQueue);
    }

    @Bean
    public Binding categoryIdBindingKey(final DirectExchange categoryExchange, final Queue categoryIdQueue){
        return BindingBuilder.bind(categoryIdQueue).to(categoryExchange).with(categoryIdBindingKey);
    }
}
