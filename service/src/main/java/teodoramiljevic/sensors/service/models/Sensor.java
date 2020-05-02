package teodoramiljevic.sensors.service.models;

import org.bson.types.ObjectId;

import java.util.List;

public class Sensor {
    private ObjectId id;
    private String sensorId;
    private List<SensorData> values;


    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public List<SensorData> getValues() {
        return values;
    }

    public void setValues(final List<SensorData> values) {
        this.values = values;
    }
}
