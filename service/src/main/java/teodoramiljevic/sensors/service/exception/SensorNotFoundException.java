package teodoramiljevic.sensors.service.exception;

/**
 * Exception describing the case of sensor with given id not found
 */
public class SensorNotFoundException extends Exception {

    private final String sensorId;

    public SensorNotFoundException(final String sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public String getMessage() {
        return "Sensor with id " + sensorId + " not found.";
    }
}
