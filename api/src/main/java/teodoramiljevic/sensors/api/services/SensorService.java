package teodoramiljevic.sensors.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.models.SensorData;
import teodoramiljevic.sensors.api.models.messages.SensorDataResponse;
import teodoramiljevic.sensors.api.services.message.MessageConsumer;
import teodoramiljevic.sensors.api.services.message.MessagePublisher;

import java.util.Optional;

@Service
public class SensorService extends SensorMessageService {
    private static final Logger logger = LogManager.getLogger(SensorService.class);

    public SensorService(final MessagePublisher messagePublisher, final MessageConsumer messageConsumer,
                         final ObjectMapper objectMapper, final ModelMapper modelMapper) {
        super(messagePublisher, messageConsumer, objectMapper, modelMapper);
    }

    public Optional<SensorDataResponse> addValueToSensor(final double value, final String sensorId){
        final SensorData sensorData = new SensorData(sensorId, value);
        final Optional<String> confirmationId = publishValue(sensorData);

        final Optional<String> confirmedValue = getValue(confirmationId);
        if(confirmedValue.isPresent()){
            return deserializeValue(confirmedValue.get(), SensorDataResponse.class);
        }

        return Optional.empty();
    }
}
