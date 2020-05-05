package teodoramiljevic.sensors.service.repository;

import teodoramiljevic.sensors.service.exception.SensorNotFoundException;
import teodoramiljevic.sensors.service.model.SensorData;

import java.util.List;
import java.util.Optional;

public interface SensorRepository {

    boolean saveValue(String sensorId, SensorData sensorData);

    Optional<SensorData> getLatestValueBySensorId(String sensorId) throws SensorNotFoundException;

    List<SensorData> getValuesBySensorId(String sensorId);
}
