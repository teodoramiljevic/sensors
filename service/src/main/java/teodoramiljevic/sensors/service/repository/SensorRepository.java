package teodoramiljevic.sensors.service.repository;

import teodoramiljevic.sensors.service.models.SensorData;

import java.util.List;
import java.util.Optional;

public interface SensorRepository {
    boolean addValue(String sensorId, SensorData sensorData);

    Optional<SensorData> getLatestValue(String sensorId);

    List<SensorData> getValues(String sensorId);
}
