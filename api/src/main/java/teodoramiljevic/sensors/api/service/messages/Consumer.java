package teodoramiljevic.sensors.api.service.messages;

import java.util.Optional;

/**
 * Describes the actions of a service that is able to consume/receive data.
 */
public interface Consumer {
    /**
     * @param consumptionId ID for correlation between sender and receiver
     * @return Consumed data
     */
    Optional<String> consume(final Optional<String> consumptionId);
}
