package teodoramiljevic.sensors.service.exception;

public class SensorNotFoundException extends Exception {

    private final String sensorId;

    public SensorNotFoundException(final String sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public String getMessage() {
        return "Sensor with id " + sensorId + " not found";
    }
}
