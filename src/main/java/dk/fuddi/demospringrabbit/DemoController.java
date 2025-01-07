package dk.fuddi.demospringrabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitStreamTemplate rabbitStreamTemplate;

    @PostMapping("/rabbit/produce")
    public void produce(@RequestBody String json) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_DEMO, RabbitConfig.ROUTING_KEY_BI, json, DemoController::setMessageId);
    }

    @PostMapping("/rabbit/stream/produce")
    public void streamProduce(@RequestBody String json) {
        rabbitStreamTemplate.convertAndSend(json, DemoController::setMessageId);
    }

    private static Message setMessageId(Message message) {
        message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
        return message;
    }
}
