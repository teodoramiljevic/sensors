package teodoramiljevic.sensors.service.service;

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

    //region Constants
    private final String FANOUT_EXCHANGE = "fanout";
    private final String DIRECT_EXCHANGE = "direct";
    private final String QUEUE_BINDING_KEY = "";
    //endregion Constants

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

            channel.exchangeDeclare(properties.getBroadcastSensorExchange(), FANOUT_EXCHANGE, true);
            channel.exchangeDeclare(properties.getDirectSensorExchange(), DIRECT_EXCHANGE, true);

            channel.queueDeclare(properties.getSensorQueue(), true, false, false, null);
            channel.queueDeclare(properties.getSensorReplyQueue(), true, false, false, null);

            channel.queueBind(properties.getSensorQueue(), properties.getDirectSensorExchange(), QUEUE_BINDING_KEY);
            channel.queueBind(properties.getSensorReplyQueue(), properties.getBroadcastSensorExchange(), QUEUE_BINDING_KEY);

            channel.close();
            connection.close();
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            connection.close();
        }

        messageConsumer.consume();
    }
}
