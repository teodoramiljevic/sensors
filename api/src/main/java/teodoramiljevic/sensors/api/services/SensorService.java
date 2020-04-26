package teodoramiljevic.sensors.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.models.SensorData;

import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private MessageService messageService;
    @Autowired
    private ObjectMapper objectMapper;

    public Optional<SensorData> addValueToSensor(final double value, final String sensorId){
        final SensorData sensorData = new SensorData(sensorId, value);

        try{
            final String sensorDataJsonValue = objectMapper.writeValueAsString(sensorData);

            final String id = messageService.publish(sensorDataJsonValue);

            if(id != null){
                final Boolean valueAdded = messageService.consume(id);
                return valueAdded?Optional.of(sensorData):Optional.empty();
            }

        }catch(final Exception ex){
            // TODO: Log and manage
        }

        return Optional.empty();
    }
}
