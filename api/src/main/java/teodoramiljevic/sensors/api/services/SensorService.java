package teodoramiljevic.sensors.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.models.SensorData;
import teodoramiljevic.sensors.api.services.message.MessageService;

import java.util.Optional;

@Service
public class SensorService {

    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public SensorService(final MessageService messageService, final ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    private static final Logger logger = LogManager.getLogger(SensorService.class);

    public Optional<SensorData> addValueToSensor(final double value, final String sensorId){
        final SensorData sensorData = new SensorData(sensorId, value);
        final Optional<String> confirmationId = publishValue(sensorData);


        final Boolean valueAdded = confirmValue(confirmationId);
        return valueAdded? Optional.of(sensorData): Optional.empty();
    }

    private Optional<String> publishValue(final SensorData sensorData){
        try{
            final String sensorDataJsonValue =  objectMapper.writeValueAsString(sensorData);
            return messageService.publish(sensorDataJsonValue);
        }
        catch(final Exception ex){

            logger.error(ex.getMessage(), ex.getStackTrace());
            return Optional.empty();
        }
    }

    private boolean confirmValue(final Optional<String> confirmationId){
        if(confirmationId.isPresent()){
            return messageService.consume(confirmationId.get());
        }

        logger.debug("Confirming thar value is added failed, publishing did not return valid ID for confirmation");
        return false;
    }
}
