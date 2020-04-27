package teodoramiljevic.sensors.api.services.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class AddValueConsumer extends DefaultConsumer {
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    private final String correlationId;
    private final BlockingQueue<String> responseQueue;
    private final Logger logger = LogManager.getLogger(AddValueConsumer.class);

    public AddValueConsumer(final Channel channel, final String correlationId, final BlockingQueue<String> responseQueue) {
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
            final String data = new String(body, "UTF-8");
            logger.debug(data);
            responseQueue.offer(data);
            this.getChannel().basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
