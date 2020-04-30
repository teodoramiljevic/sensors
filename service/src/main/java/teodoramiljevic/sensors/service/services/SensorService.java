package teodoramiljevic.sensors.service.services;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.service.configuration.AppProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Service
public class SensorService implements  AutoCloseable{
    private AppProperties properties;

    private Connection connection;
    private Channel channel;

    private final Logger logger = LogManager.getLogger();

    public SensorService(AppProperties properties) throws IOException, TimeoutException {
        this.properties = properties;

        final ConnectionFactory factory = new ConnectionFactory();
        logger.debug(properties.getRabbitHost(), properties.getRabbitPort());
        factory.setHost(properties.getRabbitHost()); //TODO: construct url

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.basicQos(1);
    }

    public void listen(){
        try{
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                logger.debug(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");

                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                channel.basicPublish(properties.getBroadcastSensorExchange(), "", replyProps, delivery.getBody());

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            channel.basicConsume(properties.getSensorQueue(), false, deliverCallback, consumerTag -> {});

        }
        catch(Exception ex){
            logger.error(ex.getMessage(), ex);
        }
    }

    public void close(){
        try{
            connection.close();
        }
        catch(final Exception ex){
            logger.error(ex.getMessage(),ex);
        }
    }
}
