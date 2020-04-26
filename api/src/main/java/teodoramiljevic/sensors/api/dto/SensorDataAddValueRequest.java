package teodoramiljevic.sensors.api.dto;

import javax.validation.constraints.NotBlank;

public class SensorDataAddValueRequest {
    //TODO: Move to translatable key
    @NotBlank(message = "Id is mandatory")
    private String id;
    private double value;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }
}
