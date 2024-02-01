package org.berka.config.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rabbitmq.productExchange}")
    private String productExchange;

    @Value("${rabbitmq.category-id-queue}")
    private String categoryIdQueue;

    @Value("${rabbitmq.storage-id-queue}")
    private String storageIdQueue;

    @Value("${rabbitmq.unit-id-queue}")
    String unitIdQueue;


    @Bean
    DirectExchange productExchange(){
        return new DirectExchange(productExchange);
    }

    @Bean
    Queue exchangeIdQueue(){
        return new Queue(unitIdQueue);
    }

    @Bean
    Queue storageIdQueue(){
        return new Queue(storageIdQueue);
    }

    @Bean
    Queue categoryIdQueue(){
        return new Queue(categoryIdQueue);
    }
}
