package teodoramiljevic.sensors.messaging;

public class RequestBase {
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    private String sensorId;

}
