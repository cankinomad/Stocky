package org.berka.rabbitmq.produce;

import lombok.RequiredArgsConstructor;
import org.berka.rabbitmq.model.RegisterUserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserProducer {

    @Value("${rabbitmq.authExchange}")
    private String authExchange;

    @Value("${rabbitmq.register-user-bindingKey}")
    private String registerUserBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void registerUser(RegisterUserModel model) {
        rabbitTemplate.convertAndSend(authExchange,registerUserBindingKey,model);
    }

}
