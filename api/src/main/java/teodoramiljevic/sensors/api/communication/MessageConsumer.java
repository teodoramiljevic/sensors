package teodoramiljevic.sensors.api.communication;

import java.util.Optional;

/**
 * Describes the actions of a service that is able to consume/receive messages.
 */
public interface MessageConsumer {
    /**
     * @param id ID for correlation between sender and receive
     * @return Consumed message
     */
    Optional<String> consume(String id);
}
