package teodoramiljevic.sensors.api.service.messages;

import java.util.Optional;

public interface Publisher {
    <T> Optional<String> publish(final T data);
}
