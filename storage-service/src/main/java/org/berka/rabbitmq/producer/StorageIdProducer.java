package org.berka.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageIdProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchangeStorage}")
    private String exchangeStorage;

    @Value("${rabbitmq.storage-id-bindingKey}")
    private String storageIdBindingKey;

    public void storageId(Long storageId){
        rabbitTemplate.convertAndSend(exchangeStorage, storageIdBindingKey, storageId);
    }
}
