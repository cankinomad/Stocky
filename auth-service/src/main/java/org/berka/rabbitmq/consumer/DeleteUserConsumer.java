package org.berka.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.berka.service.AuthService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserConsumer {

    private final AuthService authService;

    @RabbitListener(queues = "${rabbitmq.delete-user-queue}")
    public void deleteUser(String token){
        authService.deleteUser(token);
    }
}
