package dk.fuddi.demospringrabbit.consumer;

import dk.fuddi.demospringrabbit.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryLimitConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE_X_LIMIT)
    public void consume(Message message, MessageHeaders headers) {
        log.info("Consuming messageId [{}] headers [{}]", message.getMessageProperties().getMessageId(), headers);

        if (headers.containsKey("x-delivery-count")) {
            log.info("Received message with delivery limit [{}]", headers.get("x-delivery-count"));
        }

        simulateProcessing(message);
    }

    private void simulateProcessing(Message message) {
        log.info("Simulate a failure to trigger retry for messageId [{}]", message.getMessageProperties().getMessageId());
        throw new RuntimeException("XLimit processing failed");
    }
}
