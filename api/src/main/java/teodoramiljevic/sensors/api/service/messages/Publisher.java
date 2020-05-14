package teodoramiljevic.sensors.api.service.messages;

import java.util.Optional;

/**
 * Describes the actions of a service that is able to publish/send data.
 */
public interface Publisher {
    /**
     * @param data Data to be published
     * @param <T> Any type of data
     * @return Published data
     */
    <T> Optional<String> publish(final T data);
}
