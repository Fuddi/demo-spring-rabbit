package dk.fuddi.demospringrabbit.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamConsumer {

    @RabbitListener(queues = StreamConfig.STREAM_NAME_QUEUE_1)
    public void consume(String message) {
        log.info("Received message from stream: {}", message);
    }
}
