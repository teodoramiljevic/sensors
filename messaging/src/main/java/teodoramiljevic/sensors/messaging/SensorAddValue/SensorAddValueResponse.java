package teodoramiljevic.sensors.messaging.SensorAddValue;

import teodoramiljevic.sensors.messaging.ResponseBase;
import teodoramiljevic.sensors.messaging.SensorValue;

public class SensorAddValueResponse extends ResponseBase {
    private SensorValue value;

    public SensorValue getValue() {
        return value;
    }

    public void setValue(SensorValue value) {
        this.value = value;
    }
}
