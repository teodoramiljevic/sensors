package teodoramiljevic.sensors.api.services.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teodoramiljevic.sensors.api.configuration.AppProperties;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


public class RabbitMqMessageService implements AutoCloseable{

    final AppProperties properties;
    final Connection connection;
    private final Logger logger = LogManager.getLogger(RabbitMqMessageService.class);

    RabbitMqMessageService(final AppProperties properties) throws IOException, TimeoutException {

        this.properties = properties;

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getRabbitHost());

        connection = factory.newConnection();
    }

    AMQP.BasicProperties createProps(){
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
            logger.error(ex.getMessage(), ex);
        }
    }
}
