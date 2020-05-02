package teodoramiljevic.sensors.service.repository;

import teodoramiljevic.sensors.service.models.SensorData;

public interface SensorRepository {
    boolean addValue(String sensorId, SensorData sensorData);

}
