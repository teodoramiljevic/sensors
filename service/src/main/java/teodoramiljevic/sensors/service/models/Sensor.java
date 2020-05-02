package teodoramiljevic.sensors.service.models;

import java.util.List;

public class Sensor {
    private String id;
    private List<SensorData> values;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<SensorData> getValues() {
        return values;
    }

    public void setValues(final List<SensorData> values) {
        this.values = values;
    }
}
