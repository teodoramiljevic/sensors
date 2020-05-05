package teodoramiljevic.sensors.service.communication;

public interface MessagePublisher {
    boolean publish(final String message, final String correlationId);
}
