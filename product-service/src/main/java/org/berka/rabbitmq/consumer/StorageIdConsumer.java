package org.berka.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.berka.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageIdConsumer {

    private final ProductService productService;

    @RabbitListener(queues = "${rabbitmq.storage-id-queue}")
    public void storageId(Long storageId){
        productService.changeStorageToDefault(storageId);
    }
}
