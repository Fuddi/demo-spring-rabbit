package dk.fuddi.demospringrabbit.custom_retry;

import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

import java.time.Duration;

@Configuration
public class CustomRabbitListenerConfig {

    @Value("${custom-consumer.retry.enabled}")
    private boolean retryEnabled;

    @Value("${custom-consumer.retry.max-attempts}")
    private int maxAttempts;

    @Value("${custom-consumer.retry.initial-interval}")
    private Duration initialInterval;

    @Value("${custom-consumer.retry.max-interval}")
    private Duration maxInterval;

    @Value("${custom-consumer.retry.multiplier}")
    private double multiplier;

    @Bean
    public SimpleRabbitListenerContainerFactory customRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        if (retryEnabled) {
            factory.setAdviceChain(RetryInterceptorBuilder
                    .stateless()
                    .backOffPolicy(customBackOffPolicy())
                    .maxAttempts(maxAttempts)
                    .build());
        }
        return factory;
    }

    private ExponentialBackOffPolicy customBackOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialInterval.toMillis());
        backOffPolicy.setMaxInterval(maxInterval.toMillis());
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }
}
