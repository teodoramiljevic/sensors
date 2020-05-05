package teodoramiljevic.sensors.service.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ObjectMapperWrapper {

    private final Logger logger = LogManager.getLogger(ObjectMapperWrapper.class);

    private final ObjectMapper objectMapper;

    public ObjectMapperWrapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> Optional<T> readValue(final byte[] src, final Class<T> valueType) {
        try {
            final T object = objectMapper.readValue(src, valueType);
            return Optional.ofNullable(object);
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    public Optional<String> writeValueAsString(final Object value) {
        try {
            final String valueAsString = objectMapper.writeValueAsString(value);
            return Optional.of(valueAsString);
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}
