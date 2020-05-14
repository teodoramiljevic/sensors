package teodoramiljevic.sensors.service.communication;

/**
 * Describes the actions of a service that is able to publish/send messages.
 */
public interface MessagePublisher {

    /**
     * @param message Message to be published/sent
     * @param correlationId ID for correlation between sender and receiver
     * @return Confirmation of a published message
     */
    boolean publish(final String message, final String correlationId);
}
