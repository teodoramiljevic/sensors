package teodoramiljevic.sensors.api.exception;

public abstract class SensorException extends RuntimeException{
    public abstract String getKey();

    SensorException(final String message){
        super(message);
    }
}
