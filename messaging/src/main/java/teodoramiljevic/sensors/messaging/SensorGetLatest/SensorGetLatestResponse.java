package teodoramiljevic.sensors.messaging.SensorGetLatest;

import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;
import teodoramiljevic.sensors.messaging.SensorValue;

public class SensorGetLatestResponse extends ResponseBase {
    private SensorValue value;

    public SensorGetLatestResponse(){}

    public SensorGetLatestResponse(SensorValue value, MessageStatus status, String messageKey){
        super(status, messageKey);
        this.value = value;
    }

    public SensorValue getValue() {
        return value;
    }

    public void setValue(SensorValue value) {
        this.value = value;
    }
}
