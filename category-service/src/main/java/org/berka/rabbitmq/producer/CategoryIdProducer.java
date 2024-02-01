package org.berka.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryIdProducer {

    @Value("${rabbitmq.categoryExchange}")
    private String categoryExchange;

    @Value("${rabbitmq.category-id-bindingKey}")
    private String categoryIdBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void categoryId(Long categoryId) {
        rabbitTemplate.convertAndSend(categoryExchange, categoryIdBindingKey, categoryId);
    }
}
