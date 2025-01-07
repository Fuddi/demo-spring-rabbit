package dk.fuddi.demospringrabbit.stream;

import com.rabbitmq.stream.Environment;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

@Configuration
public class StreamConfig {

    public static final String STREAM_NAME_QUEUE_1 = "stream.queue1";

    @Bean
    RabbitStreamTemplate streamTemplate(Environment environment) {
        return new RabbitStreamTemplate(environment, STREAM_NAME_QUEUE_1);
    }

    @Bean
    Queue streamQueue() {
        return QueueBuilder.durable(STREAM_NAME_QUEUE_1)
                .stream()
                .build();
    }
}
