package teodoramiljevic.sensors.api.repository;

import teodoramiljevic.sensors.api.model.SensorValue;

import java.util.Optional;

/**
 * Describes action available upon repository that caches sensor values.
 */
public interface SensorCacheRepository {

    /**
     * @param sensorId ID of the sensor to get the latest value of
     * @return Latest value of the sensor
     */
    Optional<SensorValue> getLatest(String sensorId);

    /**
     * @param sensorId ID of the sensor to set the value for
     * @param value Value of the sensor
     */
    void setLatest(String sensorId, SensorValue value);
}
