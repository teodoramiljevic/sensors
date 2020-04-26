package teodoramiljevic.sensors.api.services;

public interface MessageService<T>{

    String publish(T object);

    boolean consume(final String id);
}
