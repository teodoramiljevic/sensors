package teodoramiljevic.sensors.api.models;

import java.util.ArrayList;
import java.util.List;

public class SensorDataList {
    private List<SensorValue> values;

    public List<SensorValue> getValues() {
        return this.values == null?new ArrayList<>():this.values;
    }

    public void setValues(final List<SensorValue> values) {
        this.values = values;
    }
}
