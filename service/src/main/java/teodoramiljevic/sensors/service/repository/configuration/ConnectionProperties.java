package teodoramiljevic.sensors.service.repository.configuration;

public class ConnectionProperties {

    private final String host;
    private final int port;

    public ConnectionProperties(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
