package teodoramiljevic.sensors.api.services.messages;

import java.util.Optional;

public interface Publisher {
    <T> Optional<String> publish(final T data);
}
