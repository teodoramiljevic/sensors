package teodoramiljevic.sensors.api.services.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.configuration.AppProperties;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Service
@Qualifier("default")
public class RabbitMqMessageService implements MessageService<String>, AutoCloseable{

    private final AppProperties properties;
    private final Connection connection;
    private final Logger logger = LogManager.getLogger(RabbitMqMessageService.class);

    public RabbitMqMessageService(final AppProperties properties) throws IOException, TimeoutException {

        this.properties = properties;

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getRabbitHost());

        connection = factory.newConnection();
    }

    @Override
    public Optional<String> publish(final String message) {
        try(final Channel channel = connection.createChannel()){
            final AMQP.BasicProperties props = createProps();

            channel.basicPublish(properties.getDirectSensorExchange(), "", props, message.getBytes("UTF-8"));

            return Optional.of(props.getCorrelationId());
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            return Optional.empty();
        }
    }

    public boolean consume(final String id){
        try{
            final Channel channel = connection.createChannel();

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            final String consumerTag = channel.basicConsume(properties.getSensorReplyQueue(), false, "", new AddValueConsumer(channel, id, response));

            response.take();
            channel.basicCancel(consumerTag);

            return true;
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            return false;
        }
    }

    private AMQP.BasicProperties createProps(){
        final String correlationId = UUID.randomUUID().toString();
        final AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .build();
        return props;
    }

    public void close(){
        try{
            connection.close();
        }
        catch(final Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
        }
    }
}
