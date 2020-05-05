package teodoramiljevic.sensors.api.model;

public class SensorValue {

    private long timestamp;
    private double value;

    public SensorValue(){}

    SensorValue(final long timestamp, final double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Value: " + this.value;
    }
}
