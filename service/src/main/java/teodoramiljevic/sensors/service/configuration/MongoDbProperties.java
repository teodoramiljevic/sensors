package teodoramiljevic.sensors.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.mongodb")
public class MongoDbProperties {
    private String host;
    private String port;
    private String database;
    private String collection;


    public String getDatabase() {
        return database;
    }

    public void setDatabase(final String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(final String collection) {
        this.collection = collection;
    }


    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return Integer.parseInt(this.port);
    }

    public void setPort(final String port) {
        this.port = port;
    }
}
