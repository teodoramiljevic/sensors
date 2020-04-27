package teodoramiljevic.sensors.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app")
public class AppProperties {
    private String broadcastSensorExchange;
    private String directSensorExchange;
    private String sensorQueue;
    private String rabbitHost;
    private String sensorReplyQueue;


    public String getSensorReplyQueue() {
        return sensorReplyQueue;
    }

    public void setSensorReplyQueue(final String sensorReplyQueue) {
        this.sensorReplyQueue = sensorReplyQueue;
    }

    public String getRabbitHost() {
        return rabbitHost;
    }

    public void setRabbitHost(final String rabbitHost) {
        this.rabbitHost = rabbitHost;
    }

    String getBroadcastSensorExchange() {
        return broadcastSensorExchange;
    }

    public void setBroadcastSensorExchange(final String broadcastSensorExchange) {
        this.broadcastSensorExchange = broadcastSensorExchange;
    }

    public String getDirectSensorExchange() {
        return directSensorExchange;
    }

    public void setDirectSensorExchange(final String directSensorExchange) {
        this.directSensorExchange = directSensorExchange;
    }

    String getSensorQueue() {
        return sensorQueue;
    }

    public void setSensorQueue(final String sensorQueue) {
        this.sensorQueue = sensorQueue;
    }
}
