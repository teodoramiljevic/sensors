package teodoramiljevic.sensors.messaging.SensorAddValue;

public class SensorAddValueRequest {
    private  long timestamp;
    private String sensorId;
    private  double value;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }
}
