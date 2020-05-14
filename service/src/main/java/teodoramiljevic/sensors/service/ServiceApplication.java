package teodoramiljevic.sensors.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class ServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
