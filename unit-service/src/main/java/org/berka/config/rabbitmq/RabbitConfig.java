package org.berka.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchangeUnit}")
    private String exchangeUnit;

    @Value("${rabbitmq.unit-id-queue}")
    private String unitIdQueue;

    @Value("${rabbitmq.unit-id-bindingKey}")
    private String unitIdBindingKey;


    @Bean
    DirectExchange exchangeUnit(){
        return new DirectExchange(exchangeUnit);
    }

    @Bean
    Queue exchangeIdQueue(){
        return new Queue(unitIdQueue);
    }

    @Bean
    public Binding unitIdBinding(final DirectExchange exchangeUnit,final Queue exchangeIdQueue){
        return BindingBuilder.bind(exchangeIdQueue).to(exchangeUnit).with(unitIdBindingKey);
    }
}
