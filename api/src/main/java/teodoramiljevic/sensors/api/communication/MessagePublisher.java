package teodoramiljevic.sensors.api.communication;

import java.util.Optional;

public interface MessagePublisher {
    Optional<String> publish(String message, String messageType);
}
