package teodoramiljevic.sensors.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app.rabbitmq")
public class RabbitMqProperties {
    private String broadcastSensorExchange;
    private String directSensorExchange;
    private String sensorQueue;
    private String host;
    private String sensorReplyQueue;
    private String consumerTimeout;

    public long getConsumerTimeout() {
        return Long.parseLong(consumerTimeout);
    }

    public void setConsumerTimeout(final String consumerTimeout) {
        this.consumerTimeout = consumerTimeout;
    }

    public String getSensorReplyQueue() {
        return sensorReplyQueue;
    }

    public void setSensorReplyQueue(final String sensorReplyQueue) {
        this.sensorReplyQueue = sensorReplyQueue;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
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
