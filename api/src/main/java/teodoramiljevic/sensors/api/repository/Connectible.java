package teodoramiljevic.sensors.api.repository;

/**
 * Imposes the implementation of the connect method making sure that it's invoked.
 */
public abstract class Connectible {

    final ConnectionProperties properties;

    Connectible(final ConnectionProperties properties){
        this.properties = properties;
        this.connect();
    }

    public abstract void connect();
}
