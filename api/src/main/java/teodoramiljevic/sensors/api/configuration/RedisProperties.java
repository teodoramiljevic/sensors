package teodoramiljevic.sensors.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app.redis")
public class RedisProperties {

    private String host;
    private String port;
    private String timeout;

    public int getTimeout(){
        return Integer.parseInt(timeout);
    }

    public void setTimeout(final String timeout){
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
    }
}
