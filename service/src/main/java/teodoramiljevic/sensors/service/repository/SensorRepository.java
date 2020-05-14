package teodoramiljevic.sensors.service.repository;

import teodoramiljevic.sensors.service.exception.SensorNotFoundException;
import teodoramiljevic.sensors.service.model.SensorData;

import java.util.List;
import java.util.Optional;

/**
 * Described actions that can be made upon repository holding sensor data.
 */
public interface SensorRepository {

    /**
     * @param sensorId ID of the sensor to be written to
     * @param sensorData Data for the sensor to be updated with
     * @return Confirmation of the save operation
     */
    boolean saveValue(String sensorId, SensorData sensorData);

    /**
     * @param sensorId ID of the sensor to get the latest value of
     * @return Latest value of the sensor
     * @throws SensorNotFoundException Should throw an exceptions in case the sensor is not found
     */
    Optional<SensorData> getLatestValueBySensorId(String sensorId) throws SensorNotFoundException;

    /**
     * @param sensorId ID of the sensor to get the values of
     * @return List of values of the sensor
     */
    List<SensorData> getValuesBySensorId(String sensorId);
}
