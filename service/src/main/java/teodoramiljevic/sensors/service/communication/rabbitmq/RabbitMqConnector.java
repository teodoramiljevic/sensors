package teodoramiljevic.sensors.service.communication.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teodoramiljevic.sensors.service.configuration.RabbitMqProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqConnector implements AutoCloseable {
    final RabbitMqProperties properties;
    final Connection connection;
    private final Logger logger = LogManager.getLogger(RabbitMqConnector.class);

    RabbitMqConnector(final RabbitMqProperties properties) throws IOException, TimeoutException {

        this.properties = properties;

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getHost());

        connection = factory.newConnection();
    }

    AMQP.BasicProperties createProps(final String correlationId) {
        final AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .build();
        return props;
    }

    public void close() {
        try {
            connection.close();
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}