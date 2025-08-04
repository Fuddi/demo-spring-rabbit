package dk.fuddi.demospringrabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.retry.ImmediateRequeueMessageRecoverer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_DEMO = "demo.t-x";
    public static final String DLX_DEMO = "demo.t-dlx";

    public static final String ROUTING_KEY_BI = "demo.bi";
    public static final String ROUTING_KEY_X_LIMIT_DEAD = "demo.x-limit.dead";

    public static final String DLQ = "ko.dlq";
    public static final String QUEUE_X_LIMIT = "ko.x-limit.q";

    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_DEMO).build();
    }

    @Bean
    public DirectExchange dlxDemoExchange() {
        return ExchangeBuilder.directExchange(DLX_DEMO).build();
    }

    /**
     * @link <a href="https://www.rabbitmq.com/docs/quorum-queues#poison-message-handling">Poison Message Handling</a>
     */
    @Bean
    public Queue xLimitQueue() {
        return QueueBuilder
                .durable(QUEUE_X_LIMIT)
                .deadLetterExchange(DLX_DEMO)
                .deadLetterRoutingKey(ROUTING_KEY_X_LIMIT_DEAD)
                .quorum()
/*
                .deliveryLimit(4) //Starting with RabbitMQ 4.0, the delivery limit for quorum queues defaults to 20.
                We set the delivery limit to 4 with a RabbitMQ policy "delivery-limit"
 */
                .build();
    }

    @Bean
    public Binding xLimitBinding() {
        return new Binding(QUEUE_X_LIMIT, Binding.DestinationType.QUEUE, EXCHANGE_DEMO, ROUTING_KEY_BI, null);
    }

    /**
     * Configures a MessageRecoverer that requeues failed messages back to their original queue.
     *
     * <p>This recovery strategy is suitable for Quorum queues, which provide built-in protection
     * against infinite redelivery loops through automatic delivery limits.</p>
     *
     * <h3>Delivery Limit Configuration</h3>
     * <ul>
     *   <li><strong>RabbitMQ Default:</strong> 20 redeliveries for Quorum queues (RabbitMQ 4.0+)</li>
     *   <li><strong>Current Policy:</strong> Limited to 3 redeliveries via RabbitMQ policy "deliver-limit"</li>
     * </ul>
     *
     * <p>After exceeding the delivery limit, messages are automatically moved to a dead letter queue
     * if configured, preventing infinite processing loops.</p>
     *
     * @return MessageRecoverer that immediately requeues failed messages
     * @see <a href="https://www.rabbitmq.com/docs/quorum-queues#repeated-requeues">RabbitMQ Quorum Queues - Repeated Requeues</a>
     */
    @Bean
    public MessageRecoverer messageRecoverer() {
        return new ImmediateRequeueMessageRecoverer();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DLQ)
                .quorum()
                .build();
    }

    @Bean
    public Binding xLimitDLQBinding() {
        return new Binding(DLQ, Binding.DestinationType.QUEUE, DLX_DEMO, ROUTING_KEY_X_LIMIT_DEAD, null);
    }
}
