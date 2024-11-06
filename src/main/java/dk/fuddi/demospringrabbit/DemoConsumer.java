package dk.fuddi.demospringrabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.retry.RetryContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE_BI, containerFactory = "rabbitListenerContainerFactory")
    public void consume(Message message, MessageHeaders headers) {
        log.info("Consuming message [{}]", message);

        simulateProcessing(message, null);

    }

    private void simulateProcessing(Message message, RetryContext context) {
        // Simulate a failure to trigger retry
        throw new RuntimeException("Something went wrong");
    }
}
