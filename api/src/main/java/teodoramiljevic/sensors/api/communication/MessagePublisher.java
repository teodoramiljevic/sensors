package teodoramiljevic.sensors.api.communication;

import java.util.Optional;

/**
 * Describes the actions of a service that is able to publish/send messages.
 */
public interface MessagePublisher {

    /**
     * @param message Message to be published/sent
     * @param messageType Content type of message to be published
     * @return Published message
     */
    Optional<String> publish(String message, String messageType);
}
