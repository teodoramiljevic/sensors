package teodoramiljevic.sensors.messaging.SensorAddValue;

import teodoramiljevic.sensors.messaging.RequestBase;
import teodoramiljevic.sensors.messaging.SensorValue;

public class SensorAddValueRequest extends RequestBase {
    public SensorValue getValue() {
        return value;
    }

    public void setValue(SensorValue value) {
        this.value = value;
    }

    private SensorValue value;

    public SensorAddValueRequest(String sensorId, SensorValue value){
        setSensorId(sensorId);
        this.value = value;
    }

    public SensorAddValueRequest(){}
}
