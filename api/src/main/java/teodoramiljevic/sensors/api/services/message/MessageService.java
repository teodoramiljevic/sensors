package teodoramiljevic.sensors.api.services.message;
import java.util.Optional;

public interface MessageService<T>{

    /**
     * @param object Message to be published
     * @return Identification for consuming response (optional)
     */
    Optional<String> publish(T object);

    /**
     * @param id Identification for consuming response
     * @return true if consuming was successful, false otherwise
     */
    boolean consume(final String id);
}
