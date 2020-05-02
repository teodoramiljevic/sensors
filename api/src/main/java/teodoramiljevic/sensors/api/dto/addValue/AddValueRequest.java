package teodoramiljevic.sensors.api.dto.addValue;

import javax.validation.constraints.NotBlank;

public class AddValueRequest {
    //TODO: Move to translatable key
    @NotBlank(message = "Id is mandatory")
    private String sensorId;
    private double value;

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(final String sensorId) {
        this.sensorId = sensorId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }
}
