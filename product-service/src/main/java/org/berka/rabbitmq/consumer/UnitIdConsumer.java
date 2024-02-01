package org.berka.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.berka.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitIdConsumer {

    private final ProductService productService;

    @RabbitListener(queues = "${rabbitmq.unit-id-queue}")
    public void unitId(Long unitId) {
        productService.changeUnitToDefault(unitId);
    }
}
