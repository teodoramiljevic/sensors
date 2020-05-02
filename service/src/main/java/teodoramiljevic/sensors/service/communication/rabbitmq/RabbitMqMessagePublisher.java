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

    private final Logger logger = LogManager.getLogger(RabbitMqMessagePublisher.class);

    public RabbitMqMessagePublisher(final RabbitMqProperties properties) throws IOException, TimeoutException {
        super(properties);
    }

    public boolean publish(final String message, final String correlationId) {
        try(final Channel channel = connection.createChannel()){
            final AMQP.BasicProperties props = createProps(correlationId);

            channel.basicPublish(properties.getBroadcastSensorExchange(), "", props, message.getBytes("UTF-8"));

            return true;
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }
}
