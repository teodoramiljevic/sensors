package teodoramiljevic.sensors.api.communication.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.communication.MessagePublisher;
import teodoramiljevic.sensors.api.configuration.RabbitMqProperties;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
@Qualifier("default")
public class RabbitMqMessagePublisher extends RabbitMqConnector implements MessagePublisher {

    private final Logger logger = LogManager.getLogger(RabbitMqMessagePublisher.class);

    public RabbitMqMessagePublisher(final RabbitMqProperties properties) throws IOException, TimeoutException {
        super(properties);
    }

    public Optional<String> publish(final String message, final String messageType) {
        try(final Channel channel = connection.createChannel()){
            final AMQP.BasicProperties props = createProps(messageType);

            channel.basicPublish(properties.getDirectSensorExchange(), "", props, message.getBytes("UTF-8"));

            return Optional.of(props.getCorrelationId());
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}
