package org.berka.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitIdProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchangeUnit}")
    String exchangeUnit;

    @Value("${rabbitmq.unit-id-bindingKey}")
    String unitIdBindingKey;

    public void unitId(Long unitId){
        rabbitTemplate.convertAndSend(exchangeUnit, unitIdBindingKey, unitId);
    }
}
