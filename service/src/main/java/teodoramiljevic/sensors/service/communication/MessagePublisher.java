package teodoramiljevic.sensors.service.communication;

import java.util.Optional;

public interface MessagePublisher {
    boolean publish(String message, String correlationId);
}
