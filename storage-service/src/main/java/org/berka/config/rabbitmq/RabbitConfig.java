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

    @Value("${rabbitmq.exchangeStorage}")
    private String exchangeStorage;

    @Value("${rabbitmq.storage-id-queue}")
    private String storageIdQueue;

    @Value("${rabbitmq.storage-id-bindingKey}")
    private String storageIdBindingKey;

    @Bean
    DirectExchange exchangeStorage(){
        return new DirectExchange(exchangeStorage);
    }

    @Bean
    Queue storageIdQueue(){
        return new Queue(storageIdQueue);
    }

    @Bean
    public Binding storageIdBinding(final DirectExchange exchangeStorage,final Queue storageIdQueue){
        return BindingBuilder.bind(storageIdQueue).to(exchangeStorage).with(storageIdBindingKey);
    }
}
