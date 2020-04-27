package teodoramiljevic.sensors.api.services.message;

import java.util.Optional;

public interface MessagePublisher {
    Optional<String> publish(String message);
}
