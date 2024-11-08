package dk.fuddi.demospringrabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DemoController {
    private final RabbitTemplate rabbitTemplate;

    public DemoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/rabbit/produce")
    public void produce(@RequestBody String json) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_DEMO, RabbitConfig.ROUTING_KEY_BI, json, DemoController::setMessageId);
    }

    private static Message setMessageId(Message message) {
        message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
        return message;
    }
}
