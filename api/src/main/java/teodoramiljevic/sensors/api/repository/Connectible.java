package teodoramiljevic.sensors.api.repository;

public abstract class Connectible {

    final ConnectionProperties properties;

    Connectible(final ConnectionProperties properties){
        this.properties = properties;
        this.connect();
    }

    public abstract void connect();
}
