package teodoramiljevic.sensors.api.model;

public class SensorData{

    private SensorValue value;

    public SensorValue getValue() {
        return value;
    }

    public void setValue(final SensorValue value) {
        this.value = value;
    }

    public SensorData(){}

    public SensorData(final double value) {
        this.value = new SensorValue(System.currentTimeMillis(), value);
    }

    public SensorData(final SensorValue value) {
        this.value = value;
    }
}
