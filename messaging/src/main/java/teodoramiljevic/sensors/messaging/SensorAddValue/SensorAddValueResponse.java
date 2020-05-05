package teodoramiljevic.sensors.messaging.SensorAddValue;

import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;
import teodoramiljevic.sensors.messaging.SensorValue;

public class SensorAddValueResponse extends ResponseBase {
    private SensorValue value;

    public SensorAddValueResponse(){

    }

    public SensorAddValueResponse(SensorValue value, MessageStatus status, String messageKey){
        super(status, messageKey);
        this.setValue(value);
    }

    public SensorValue getValue() {
        return value;
    }

    public void setValue(SensorValue value) {
        this.value = value;
    }
}
