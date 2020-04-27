package teodoramiljevic.sensors.api.models;

public class SensorData {
    private final long timestamp;
    private final String sensorId;
    private final double value;

    public SensorData(final String sensorId, final double value) {
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getValue() {
        return value;
    }
}
