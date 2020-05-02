package teodoramiljevic.sensors.service.communication.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueRequest;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueResponse;
import teodoramiljevic.sensors.service.communication.MessagePublisher;
import teodoramiljevic.sensors.service.models.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;
import teodoramiljevic.sensors.service.wrappers.ClassWrapper;

import java.io.IOException;
import java.util.Optional;

public class RabbitMqMessageHandler extends DefaultConsumer {
    private final Logger logger = LogManager.getLogger(RabbitMqMessageHandler.class);
    ObjectMapper objectMapper;
    ModelMapper modelMapper;
    MessagePublisher messagePublisher;
    SensorRepository repository;

    RabbitMqMessageHandler(final Channel channel, final MessagePublisher messagePublisher, final SensorRepository repository) {
        super(channel);
        objectMapper = new ObjectMapper(); // TODO: INJECT
        modelMapper = new ModelMapper();
        this.messagePublisher = messagePublisher;
        this.repository = repository;
    }

    public void handleDelivery(final String consumerTag,
                               final Envelope envelope,
                               final AMQP.BasicProperties properties,
                               final byte[] body)
            throws IOException {
        final String data = new String(body, "UTF-8");
        logger.debug("Received raw data " + data);

        final Optional<Class<?>> requestClass = ClassWrapper.forName(properties.getContentType());

        if (requestClass.isPresent()) {
            logger.debug("Received data with type of " + requestClass.get().getName());
            final Class<?> classValue = requestClass.get();
            final Object request = objectMapper.readValue(body, classValue);

            // TODO: Move logic to instance working with this type of request
            if (request instanceof SensorAddValueRequest) {
                logger.debug("Received add value request");


                final boolean valueAdded = repository.addValue(((SensorAddValueRequest) request).getSensorId(), modelMapper.map(request, SensorData.class));

                if (valueAdded) {
                    logger.debug("Value successfully added");
                    final SensorAddValueResponse sensorAddValueResponse = modelMapper.map(request, SensorAddValueResponse.class);
                    sensorAddValueResponse.setStatus(MessageStatus.SUCCESS);

                    final String response = objectMapper.writeValueAsString(sensorAddValueResponse);
                    messagePublisher.publish(response, properties.getCorrelationId());
                }
            }
        } else {
            logger.debug("Message type of the request is not supported");
            // TODO: Response class should have data and status as separate fields
            messagePublisher.publish(objectMapper.writeValueAsString(new ResponseBase(MessageStatus.INTERNAL_ERROR)), properties.getCorrelationId());
        }

        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }
}
