package teodoramiljevic.sensors.messaging.SensorAddValue;

import teodoramiljevic.sensors.messaging.ResponseBase;

public class SensorAddValueResponse extends ResponseBase {
    private  long timestamp;
    private  double value;

    public SensorAddValueResponse(){}

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
}
