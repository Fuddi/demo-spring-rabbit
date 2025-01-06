package dk.fuddi.demospringrabbit.ttl;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTLRabbitConfig {

    public static final String MAIN_QUEUE = "mainQueue";
    public static final String DLX_EXCHANGE = "dlxExchange";
    public static final String DLQ = "dlq";

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-message-ttl", 60000) // Message TTL in milliseconds (e.g., 60 seconds)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(dlxExchange()).with(DLQ);
    }
}
