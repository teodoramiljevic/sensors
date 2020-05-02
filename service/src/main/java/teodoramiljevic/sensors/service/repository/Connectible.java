package teodoramiljevic.sensors.service.repository;

import teodoramiljevic.sensors.service.models.repository.ConnectionProperties;

public abstract class Connectible {

    protected final ConnectionProperties connectionProperties;

    public Connectible(final ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
        connect();
    }

    public abstract void connect();
}
