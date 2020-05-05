package teodoramiljevic.sensors.api.dto.getValues;

import teodoramiljevic.sensors.api.dto.SensorValue;

import java.util.List;

public class GetValuesResponse {
    private final List<SensorValue> sensorValues;

    public GetValuesResponse(final List<SensorValue> sensorValues) {
        this.sensorValues = sensorValues;
    }

    public List<SensorValue> getSensorValues() {
        return sensorValues;
    }
}
