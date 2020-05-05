package teodoramiljevic.sensors.service.communication.rabbitmq;

import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.service.communication.MessageConsumer;
import teodoramiljevic.sensors.service.configuration.RabbitMqProperties;
import teodoramiljevic.sensors.service.service.requestHandler.RequestHandlerFactory;
import teodoramiljevic.sensors.service.wrapper.ObjectMapperWrapper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@Qualifier("default")
public class RabbitMqMessageConsumer extends RabbitMqConnector implements MessageConsumer {

    //region Constants
    private final int MESSAGE_PROCESSING_LIMIT = 1;
    private final String CONSUMER_TAG = "";
    //endregion Constants

    private final Logger logger = LogManager.getLogger(RabbitMqMessageConsumer.class);

    private final RabbitMqMessagePublisher messagePublisher;
    private final RequestHandlerFactory requestHandlerFactory;
    private final ObjectMapperWrapper objectMapper;

    RabbitMqMessageConsumer(final RabbitMqProperties properties,
                            final RabbitMqMessagePublisher messagePublisher,
                            final RequestHandlerFactory requestHandlerFactory,
                            final ObjectMapperWrapper objectMapper) throws IOException, TimeoutException {
        super(properties);
        this.messagePublisher = messagePublisher;
        this.requestHandlerFactory = requestHandlerFactory;
        this.objectMapper = objectMapper;
    }

    public void consume() {
        try {

            final Channel channel = connection.createChannel();
            channel.basicQos(MESSAGE_PROCESSING_LIMIT);
            channel.basicConsume(properties.getSensorQueue(), false, CONSUMER_TAG,
                    new RabbitMqMessageHandler(channel, messagePublisher, requestHandlerFactory, objectMapper));

        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
