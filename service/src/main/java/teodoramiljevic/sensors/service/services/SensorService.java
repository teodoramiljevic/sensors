package teodoramiljevic.sensors.service.services;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;


@Service
public class SensorService implements  AutoCloseable{
    private Connection connection;
    Channel channel;
    private final String replyQueueName = "SENSOR_REPLY_QUEUE";
    public SensorService(){
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        setConnection(factory);
    }

    public void listen(){
        try{

            channel.queuePurge("SENSOR_QUEUE");

            channel.queueDeclare("SENSOR_QUEUE", false, false, false,null);
            channel.exchangeDeclare("SENSOR_EXCHANGE", "direct");
            channel.exchangeDeclare("SENSOR_EXCHANGE_FAN", "fanout");
            channel.queueBind("SENSOR_QUEUE", "SENSOR_EXCHANGE", "");
            channel.queueDeclare(replyQueueName,false, false, false, null);
            channel.queueBind(replyQueueName, "SENSOR_EXCHANGE_FAN", "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");

                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                channel.basicPublish("SENSOR_EXCHANGE_FAN", "", replyProps, delivery.getBody());

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

          //  final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            channel.basicConsume("SENSOR_QUEUE", false, deliverCallback, consumerTag -> {});

 //            response.take();
  //          channel.basicCancel(ctag);

        }
        catch(Exception ex){

        }
    }

    private void setConnection(final ConnectionFactory factory){

        try{
            if(factory!= null) {
                connection = factory.newConnection();
                channel = connection.createChannel();
                channel.basicQos(1);
            }
        }
        catch(final Exception ex){
            //TODO: Log and manage
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
