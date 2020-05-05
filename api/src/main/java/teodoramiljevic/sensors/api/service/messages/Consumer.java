package teodoramiljevic.sensors.api.service.messages;

import java.util.Optional;

public interface Consumer {
    Optional<String> consume(final Optional<String> consumptionId);
}
