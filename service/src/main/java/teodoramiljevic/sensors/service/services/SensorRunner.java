package teodoramiljevic.sensors.service.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import teodoramiljevic.sensors.service.configuration.AppProperties;

@Component
public class SensorRunner implements CommandLineRunner {

    @Autowired
    private SensorService sensorService;
    @Autowired
    private AppProperties properties;

    private final Logger logger = LogManager.getLogger();

     Connection connection;
     Channel channel;

    @Override
    public void run(String... args) throws Exception {

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getRabbitHost());

        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.exchangeDeclare(properties.getBroadcastSensorExchange(), "fanout");
            channel.exchangeDeclare(properties.getDirectSensorExchange(), "direct");

            channel.queueDeclare(properties.getSensorQueue(),true, false, false, null);
            channel.queueDeclare(properties.getSensorReplyQueue(),true, false, false, null);

            channel.queueBind(properties.getSensorQueue(), properties.getDirectSensorExchange(),"");
            channel.queueBind(properties.getSensorReplyQueue(), properties.getBroadcastSensorExchange(),"");

            channel.close();
            connection.close();
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex);
            connection.close();
        }

        sensorService.listen();
    }
}
