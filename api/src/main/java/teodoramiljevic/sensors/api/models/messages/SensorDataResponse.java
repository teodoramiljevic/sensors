package teodoramiljevic.sensors.api.models.messages;

public class SensorDataResponse {

    private long timestamp;
    private String sensorId;
    private  double value;

    public SensorDataResponse(){}

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

    public void setValue(final double value) {
        this.value = value;
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
