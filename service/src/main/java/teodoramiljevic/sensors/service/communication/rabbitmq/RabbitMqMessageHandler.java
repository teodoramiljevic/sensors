package teodoramiljevic.sensors.service.communication.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;
import teodoramiljevic.sensors.service.communication.MessagePublisher;
import teodoramiljevic.sensors.service.services.requestHandler.RequestHandler;
import teodoramiljevic.sensors.service.services.requestHandler.RequestHandlerFactory;
import teodoramiljevic.sensors.service.wrappers.ClassWrapper;
import teodoramiljevic.sensors.service.wrappers.ObjectMapperWrapper;

import java.io.IOException;
import java.util.Optional;

public class RabbitMqMessageHandler extends DefaultConsumer {
    private final Logger logger = LogManager.getLogger(RabbitMqMessageHandler.class);

    private final ObjectMapperWrapper objectMapper;
    private final MessagePublisher messagePublisher;
    private final RequestHandlerFactory requestHandlerFactory;

    RabbitMqMessageHandler(final Channel channel, final MessagePublisher messagePublisher, final RequestHandlerFactory requestHandlerFactory, final ObjectMapperWrapper objectMapper) {
        super(channel);
        this.messagePublisher = messagePublisher;
        this.requestHandlerFactory = requestHandlerFactory;
        this.objectMapper = objectMapper;
    }

    public void handleDelivery(final String consumerTag,
                               final Envelope envelope,
                               final AMQP.BasicProperties properties,
                               final byte[] body)
            throws IOException {

        final Optional<Class<?>> requestClass = ClassWrapper.forName(properties.getContentType());

        String response = null;
        if (requestClass.isPresent()) {
            response = handleRequest(requestClass.get(), body);
        } else {
            logger.debug("Message type of the request is not supported");
            final Optional<String> responseValue = objectMapper.writeValueAsString(new ResponseBase(MessageStatus.INTERNAL_ERROR));
            if (responseValue.isPresent()) {
                response = responseValue.get();
            }
        }

        messagePublisher.publish(response, properties.getCorrelationId());
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }

    private String handleRequest(final Class requestClass, final byte[] body) {
        logger.debug("Received data with type of " + requestClass.getName());
        final Class<?> classValue = requestClass;
        final Optional<?> request = objectMapper.readValue(body, classValue);

        if (request.isPresent()) {
            final Optional<RequestHandler> handler = requestHandlerFactory.getHandler(classValue);
            if (handler.isPresent()) {
                final Object handledResponse = handler.get().handle(request.get());
                final Optional<String> responseValue = objectMapper.writeValueAsString(handledResponse);
                if (responseValue.isPresent()) {
                    return responseValue.get();
                }
            }

            logger.debug("No handler found for request of type " + requestClass.getName());
        }

        logger.debug("Request body couldn't be parsed to the given type " + requestClass.getName());
        return null;
    }
}
