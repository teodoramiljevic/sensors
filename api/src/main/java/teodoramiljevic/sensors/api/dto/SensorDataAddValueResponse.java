package teodoramiljevic.sensors.api.dto;

public class SensorDataAddValueResponse {
    private double value;
    private long timestamp;

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setValue(final double value) {
        this.value = value;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
}
