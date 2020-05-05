package teodoramiljevic.sensors.api.communication;

import java.util.Optional;

public interface MessageConsumer {
    Optional<String> consume(String id);
}
