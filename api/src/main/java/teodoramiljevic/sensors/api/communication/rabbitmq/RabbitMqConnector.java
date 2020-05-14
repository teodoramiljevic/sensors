package teodoramiljevic.sensors.api.communication.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teodoramiljevic.sensors.api.configuration.RabbitMqProperties;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Connects to an instance of RabbitMq.
 * Responsible of closing the connection to RabbitMq.
 * Will throw and exception if the connection is not possible.
 */
public class RabbitMqConnector implements AutoCloseable{

    private final Logger logger = LogManager.getLogger(RabbitMqConnector.class);

    final RabbitMqProperties properties;
    final Connection connection;

    RabbitMqConnector(final RabbitMqProperties properties) throws IOException, TimeoutException {

        this.properties = properties;

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getHost());

        connection = factory.newConnection();
    }

    /**
     * Creates properties necessary for publishing messages to the queue.
     * @param contentType Class name of a request
     * @return Prepared properties.
     */
    AMQP.BasicProperties createProperties(final String contentType){
        final String correlationId = UUID.randomUUID().toString();
        final AMQP.BasicProperties properties = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .contentType(contentType)
                .build();

        return properties;
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
