package teodoramiljevic.sensors.api.services.message;

import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.configuration.AppProperties;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Service
@Qualifier("default")
public class RabbitMqMessageConsumer extends RabbitMqMessageService implements MessageConsumer{

    private final Logger logger = LogManager.getLogger(RabbitMqMessagePublisher.class);

    RabbitMqMessageConsumer(final AppProperties properties) throws IOException, TimeoutException {
        super(properties);
    }

    public Optional<String> consume(final String id){
        try{
            final Channel channel = connection.createChannel();

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            final String consumerTag = channel.basicConsume(properties.getSensorReplyQueue(), false, "", new RabbitMqValueHandler(channel, id, response));

            final String result = response.take();
            channel.basicCancel(consumerTag);

            return Optional.of(result);
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            return Optional.empty();
        }
    }
}
