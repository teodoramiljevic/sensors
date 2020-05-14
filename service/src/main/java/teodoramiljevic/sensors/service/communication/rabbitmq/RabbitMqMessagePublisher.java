package teodoramiljevic.sensors.service.communication.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.service.communication.MessagePublisher;
import teodoramiljevic.sensors.service.configuration.RabbitMqProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@Qualifier("default")
public class RabbitMqMessagePublisher extends RabbitMqConnector implements MessagePublisher {

    //region Constants
    private final String ROUTING_KEY = "";
    private final String ENCODING = "UTF-8";
    //endregion Constants

    private final Logger logger = LogManager.getLogger(RabbitMqMessagePublisher.class);

    public RabbitMqMessagePublisher(final RabbitMqProperties properties) throws IOException, TimeoutException {
        super(properties);
    }

    public boolean publish(final String message, final String correlationId) {
        try (final Channel channel = connection.createChannel()) {
            final AMQP.BasicProperties props = createProperties(correlationId);

            channel.basicPublish(properties.getBroadcastSensorExchange(), ROUTING_KEY, props, message.getBytes(ENCODING));

            return true;
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }
}
