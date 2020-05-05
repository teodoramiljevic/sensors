package teodoramiljevic.sensors.api.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class NotFoundException extends SensorException {

    private final String key;

    public NotFoundException(final String key){
        super("Item not found");
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
