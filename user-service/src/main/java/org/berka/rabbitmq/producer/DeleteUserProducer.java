package org.berka.rabbitmq.producer;

import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.delete-user-bindingKey}")
    private String deleteUserBindingKey;

    @Value("${rabbitmq.userExchange}")
    private String exchangeUser;

    public void deleteUser(String token){
        rabbitTemplate.convertAndSend(exchangeUser,deleteUserBindingKey,token);
    }

}
