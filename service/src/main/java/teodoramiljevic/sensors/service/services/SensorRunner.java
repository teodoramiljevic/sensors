package teodoramiljevic.sensors.service.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import teodoramiljevic.sensors.service.communication.MessageConsumer;
import teodoramiljevic.sensors.service.configuration.RabbitMqProperties;

@Component
public class SensorRunner implements CommandLineRunner {

    @Autowired
    private MessageConsumer messageConsumer;
    @Autowired
    private RabbitMqProperties properties;

    private final Logger logger = LogManager.getLogger();

    Connection connection;

    //TODO: Should me moved to a script
    @Override
    public void run(final String... args) throws Exception {

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getHost());

        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.exchangeDeclare(properties.getBroadcastSensorExchange(), "fanout", true);
            channel.exchangeDeclare(properties.getDirectSensorExchange(), "direct", true);

            channel.queueDeclare(properties.getSensorQueue(), true, false, false, null);
            channel.queueDeclare(properties.getSensorReplyQueue(), true, false, false, null);

            channel.queueBind(properties.getSensorQueue(), properties.getDirectSensorExchange(), "");
            channel.queueBind(properties.getSensorReplyQueue(), properties.getBroadcastSensorExchange(), "");

            channel.close();
            connection.close();
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            connection.close();
        }

        messageConsumer.consume();
    }
}
