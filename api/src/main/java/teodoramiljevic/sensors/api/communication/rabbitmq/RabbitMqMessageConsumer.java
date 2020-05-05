package teodoramiljevic.sensors.api.communication.rabbitmq;

import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.communication.MessageConsumer;
import teodoramiljevic.sensors.api.configuration.RabbitMqProperties;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@Qualifier("default")
public class RabbitMqMessageConsumer extends RabbitMqConnector implements MessageConsumer {

    private final Logger logger = LogManager.getLogger(RabbitMqMessageConsumer.class);

    RabbitMqMessageConsumer(final RabbitMqProperties properties) throws IOException, TimeoutException {
        super(properties);
    }

    public Optional<String> consume(final String id){
        try{
            final Channel channel = connection.createChannel();

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            final String consumerTag = channel.basicConsume(properties.getSensorReplyQueue(), false, "", new RabbitMqMessageHandler(channel, id, response));

            final Optional<String> result = Optional.ofNullable(response.poll(properties.getConsumerTimeout(), MILLISECONDS));
            if(result.isEmpty()){
                logger.debug("Consume failed with timeout of " + properties.getConsumerTimeout() + "ms");
            }

            channel.basicCancel(consumerTag);
            return result;
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}
