package org.berka.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.berka.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryIdConsumer {

    private final ProductService productService;

    @RabbitListener(queues = "${rabbitmq.category-id-queue}")
    public void changeCategoryId(Long categoryId){
        productService.changeCategoryToDefault(categoryId);
    }
}
