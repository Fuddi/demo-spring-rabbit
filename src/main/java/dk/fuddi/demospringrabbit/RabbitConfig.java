package dk.fuddi.demospringrabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_DEMO = "demo.t-x";
    public static final String DLX_DEMO = "demo.t-dlx";

    public static final String ROUTING_KEY_BI = "demo.bi";

    public static final String ROUTING_KEY_BI_DEAD = "demo.bi.dead";
    public static final String ROUTING_KEY_X_LIMIT_DEAD = "demo.x-limit.dead";

    public static final String QUEUE_BI = "ko.bi.q";

    public static final String DLQ = "ko.dlq";

    public static final String QUEUE_X_LIMIT_ = "ko.x-limit.q";

    @Bean
    public TopicExchange klubOddsetExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_DEMO).build();
    }

    @Bean
    public DirectExchange dlxDemoExchange() {
        return ExchangeBuilder.directExchange(DLX_DEMO).build();
    }

    @Bean
    public Queue biQueue() {
        return QueueBuilder
                .durable(QUEUE_BI)
                .deadLetterExchange(DLX_DEMO)
                .deadLetterRoutingKey(ROUTING_KEY_BI_DEAD)
                .quorum()
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DLQ)
                .quorum()
                .build();
    }

    @Bean
    public Binding biBinding() {
        return new Binding(QUEUE_BI, Binding.DestinationType.QUEUE, EXCHANGE_DEMO, ROUTING_KEY_BI, null);
    }

    @Bean
    public Binding biDLQBinding() {
        return new Binding(DLQ, Binding.DestinationType.QUEUE, DLX_DEMO, ROUTING_KEY_BI_DEAD, null);
    }

    @Bean
    public Queue xLimitQueue() {
        return QueueBuilder
                .durable(QUEUE_X_LIMIT_)
                .deadLetterExchange(DLX_DEMO)
                .deadLetterRoutingKey(ROUTING_KEY_X_LIMIT_DEAD)
                .quorum()
                .deliveryLimit(4)
                .build();
    }


    @Bean
    public Binding xLimitBinding() {
        return new Binding(QUEUE_X_LIMIT_, Binding.DestinationType.QUEUE, EXCHANGE_DEMO, ROUTING_KEY_BI, null);
    }

    @Bean
    public Binding xLimitDLQBinding() {
        return new Binding(DLQ, Binding.DestinationType.QUEUE, DLX_DEMO, ROUTING_KEY_X_LIMIT_DEAD, null);
    }
}
