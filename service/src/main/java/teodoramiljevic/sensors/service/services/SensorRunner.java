package teodoramiljevic.sensors.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SensorRunner implements CommandLineRunner {

    @Autowired
    private SensorService sensorService;

    @Override
    public void run(String... args) throws Exception {
        sensorService.listen();
    }
}
