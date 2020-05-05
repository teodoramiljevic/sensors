package teodoramiljevic.sensors.messaging.SensorGetValues;

import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;
import teodoramiljevic.sensors.messaging.SensorValue;

import java.util.List;

public class SensorsGetValuesResponse extends ResponseBase {
    private List<SensorValue> values;

    public SensorsGetValuesResponse(){}

    public SensorsGetValuesResponse(List<SensorValue> values, MessageStatus status, String messageKey) {
        super(status, messageKey);
        this.values = values;
    }

    public List<SensorValue> getValues() {
        return values;
    }

    public void setValues(List<SensorValue> values) {
        this.values = values;
    }
}
