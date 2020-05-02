package teodoramiljevic.sensors.service.models;

public class SensorData {
    private double value;
    private long timestamp;

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
}
