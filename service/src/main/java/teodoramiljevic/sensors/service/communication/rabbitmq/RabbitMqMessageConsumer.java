package teodoramiljevic.sensors.service.communication.rabbitmq;

import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.service.communication.MessageConsumer;
import teodoramiljevic.sensors.service.configuration.RabbitMqProperties;
import teodoramiljevic.sensors.service.repository.SensorRepository;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@Qualifier("default")
public class RabbitMqMessageConsumer extends RabbitMqConnector implements MessageConsumer {

    private final Logger logger = LogManager.getLogger(RabbitMqMessageConsumer.class);
    private final RabbitMqMessagePublisher messagePublisher;
    private final SensorRepository repository;

    RabbitMqMessageConsumer(final RabbitMqProperties properties, final RabbitMqMessagePublisher messagePublisher, final SensorRepository sensorRepository) throws IOException, TimeoutException {
        super(properties);
        this.messagePublisher = messagePublisher;
        this.repository = sensorRepository;
    }

    public void consume() {
        try {
            final Channel channel = connection.createChannel();
            channel.basicQos(1);
            channel.basicConsume(properties.getSensorQueue(), false, "", new RabbitMqMessageHandler(channel, messagePublisher, repository));
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
