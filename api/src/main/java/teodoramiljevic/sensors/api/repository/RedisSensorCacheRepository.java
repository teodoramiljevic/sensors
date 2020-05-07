package teodoramiljevic.sensors.api.repository;

import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.configuration.RedisProperties;
import teodoramiljevic.sensors.api.model.SensorValue;
import teodoramiljevic.sensors.api.wrapper.ObjectMapperWrapper;

import java.util.Optional;

@Service
public class RedisSensorCacheRepository extends RedisCacheRepository implements SensorCacheRepository {

    private final ObjectMapperWrapper objectMapper;

    public RedisSensorCacheRepository(final RedisProperties properties, final ObjectMapperWrapper objectMapper) {
        super(properties);
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<SensorValue> getLatest(final String sensorId) {
        final String value = get(sensorId);

        if(value != null){
            return objectMapper.readValue(value, SensorValue.class);
        }

        return Optional.empty();
    }

    @Override
    public void setLatest(final String sensorId, final SensorValue value) {
        final Optional<String> serializedValue = objectMapper.writeValueAsString(value);

        if(serializedValue.isPresent()){
            set(sensorId, serializedValue.get());
        }
    }
}
