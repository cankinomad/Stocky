package org.berka.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.berka.rabbitmq.model.RegisterUserModel;
import org.berka.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserConsumer {

    private final UserService userService;

    @RabbitListener(queues = "${rabbitmq.register-user-queue}")
    public void saveUserModel(RegisterUserModel model) {
        userService.registerUser(model);
    }
}
