package teodoramiljevic.sensors.api.services;

import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.models.SensorData;

import java.util.Optional;

@Service
public class SensorService {

    public Optional<SensorData> addValueToSensor(final double value, final String sensorId){
        final SensorData sensorData = new SensorData(sensorId, value);

        return Optional.ofNullable(sensorData);
    }
}
