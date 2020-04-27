package teodoramiljevic.sensors.service.services;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.service.configuration.AppProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Service
public class SensorService implements  AutoCloseable{

    @Autowired
    private AppProperties properties;

    private Connection connection;
    private Channel channel;

    private final Logger logger = LogManager.getLogger();

    public SensorService() throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

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

        }
    }

    public void close(){
        try{
            connection.close();
        }
        catch(final Exception ex){

        }
    }
}
