package dk.fuddi.demospringrabbit.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DLQLogConsumer {

    //    @RabbitListener(queues = RabbitConfig.DLQ_BI, ackMode = "MANUAL")
    public void receiveMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Received message in DLQ (logging only): {}", new String(message.getBody()));

            // Manually acknowledge without consuming
            channel.basicNack(deliveryTag, false, true);
        } catch (Exception e) {
            log.error("Error processing message from DLQ: ", e);
        }
    }
}
