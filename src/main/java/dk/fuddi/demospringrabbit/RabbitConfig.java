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

    public static final String QUEUE_BI = "ko.bi.q";
    public static final String DLQ_BI = "ko.bi.dlq";

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
    public Queue biDeadLetterQueue() {
        return QueueBuilder
                .durable(DLQ_BI)
                .quorum()
                .build();
    }

    @Bean
    public Binding biBinding() {
        return new Binding(QUEUE_BI, Binding.DestinationType.QUEUE, EXCHANGE_DEMO, ROUTING_KEY_BI, null);
    }

    @Bean
    public Binding biDLQBinding() {
        return new Binding(DLQ_BI, Binding.DestinationType.QUEUE, DLX_DEMO, ROUTING_KEY_BI_DEAD, null);
    }

}
