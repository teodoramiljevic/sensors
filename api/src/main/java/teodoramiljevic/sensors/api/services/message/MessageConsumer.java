package teodoramiljevic.sensors.api.services.message;

import java.util.Optional;

public interface MessageConsumer {
    Optional<String> consume(String id);
}
