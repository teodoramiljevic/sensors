package teodoramiljevic.sensors.api.repository;

import teodoramiljevic.sensors.api.model.SensorValue;

import java.util.Optional;

public interface SensorCacheRepository {

    Optional<SensorValue> getLatest(String sensorId);

    void setLatest(String sensorId, SensorValue value);
}
