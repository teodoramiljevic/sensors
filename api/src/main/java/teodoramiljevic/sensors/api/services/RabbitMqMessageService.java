package teodoramiljevic.sensors.api.services;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
@Qualifier("default")
public class RabbitMqMessageService implements MessageService<String>, AutoCloseable{

    private Connection connection;
    private final String replyQueueName = "SENSOR_REPLY_QUEUE";
    public RabbitMqMessageService(){
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        setConnection(factory);
    }

    private void setConnection(final ConnectionFactory factory){

        try{
            if(factory!= null) {
                connection = factory.newConnection();
            }
        }
        catch(final Exception ex){
            //TODO: Log and manage
        }
    }


    //TODO: Pass queue/exchange names through configuration
    @Override
    public String publish(final String message) {
        try(final Channel channel = connection.createChannel()){
            final AMQP.BasicProperties props = createProps(channel);

            channel.exchangeDeclare("SENSOR_EXCHANGE", "direct");
            channel.basicPublish("SENSOR_EXCHANGE", "", props, message.getBytes("UTF-8"));

            return props.getCorrelationId();
        }
        catch (final Exception ex){
            // TODO Log and manage
            return null;
        }
    }

    //TODO: add consume broadcast/publishbroadcast
    public boolean consume(final String id){
        try{
            final Channel channel = connection.createChannel();

            channel.exchangeDeclare("SENSOR_EXCHANGE_FAN", "fanout");
            channel.queueDeclare(replyQueueName,false, false, false, null);
            channel.queueBind(replyQueueName,"SENSOR_EXCHANGE_FAN","");

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            final String ctag = channel.basicConsume(replyQueueName, false, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(id)) {

                    System.out.println(new String(delivery.getBody()));
                    response.offer(new String(delivery.getBody(), "UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    //response.offer(new String(delivery.getBody(), "UTF-8"));
                }
            }, consumerTag -> {
            });

            response.take();
            channel.basicCancel(ctag);

            return true;
        }
        catch (final Exception ex){
            // TODO Log and manage
            return false;
        }
    }

    private AMQP.BasicProperties createProps(final Channel channel){
        final String correlationId = UUID.randomUUID().toString();
        final AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                //.replyTo(getReplyQueueName(channel))
                .build();
        return props;
    }

    public void close(){
        try{
            connection.close();
        }
        catch(final Exception ex){

        }
    }
}
