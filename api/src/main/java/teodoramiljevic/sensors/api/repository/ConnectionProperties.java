package teodoramiljevic.sensors.api.repository;

public class ConnectionProperties {

    private final String host;
    private final String port;

    ConnectionProperties(final String host, final String port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}
