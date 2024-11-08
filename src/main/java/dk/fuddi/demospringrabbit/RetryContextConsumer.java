package dk.fuddi.demospringrabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class RetryContextConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE_BI)
    public void consume(Message message, MessageHeaders headers) {
        log.info("Consuming messageId [{}] headers [{}]", message.getMessageProperties().getMessageId(), headers);

        RetryContext context = RetrySynchronizationManager.getContext();
        if (Objects.requireNonNull(context).getRetryCount() > 0) {
            log.info("Retry count [{}] processing messageId [{}]", context.getRetryCount(), message.getMessageProperties().getMessageId());
        }

        simulateProcessing(message);
    }

    private void simulateProcessing(Message message) {
        log.info("Simulate a failure to trigger retry for messageId [{}]", message.getMessageProperties().getMessageId());
        throw new RuntimeException("Something went wrong");
    }
}
