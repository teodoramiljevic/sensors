package teodoramiljevic.sensors.api.communication.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Responsible for handling the requests received from the queue.
 */
public class RabbitMqMessageHandler extends DefaultConsumer {

    //region Constants
    private final String ENCODING = "UTF-8";
    //endregion Constants

    private final String correlationId;
    private final BlockingQueue<String> responseQueue;
    private final Logger logger = LogManager.getLogger(RabbitMqMessageHandler.class);

    RabbitMqMessageHandler(final Channel channel,
                           final String correlationId,
                           final BlockingQueue<String> responseQueue) {
        super(channel);
        this.correlationId = correlationId;
        this.responseQueue = responseQueue;
    }

    public void handleDelivery(final String consumerTag,
                               final Envelope envelope,
                               final AMQP.BasicProperties properties,
                               final byte[] body)
            throws IOException
    {
        if (properties.getCorrelationId().equals(correlationId)) {
            final String data = new String(body, ENCODING);
            logger.info(data);
            responseQueue.offer(data);
            this.getChannel().basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
