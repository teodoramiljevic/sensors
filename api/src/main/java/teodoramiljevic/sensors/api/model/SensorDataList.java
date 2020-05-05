package teodoramiljevic.sensors.api.model;

import java.util.ArrayList;
import java.util.List;

public class SensorDataList {
    private List<SensorValue> values;

    public SensorDataList(){
        this.values = new ArrayList<>();
    }

    public List<SensorValue> getValues() {
        return this.values;
    }

    public void setValues(final List<SensorValue> values) {
        this.values = values;
    }
}
