package teodoramiljevic.sensors.api.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.models.SensorData;
import teodoramiljevic.sensors.api.services.messages.MessageService;
import teodoramiljevic.sensors.api.services.messages.ResponseHandler;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueResponse;

import java.util.Optional;

@Service
public class SensorService{
    private static final Logger logger = LogManager.getLogger(SensorService.class);
    private final MessageService messageService;
    private final ResponseHandler responseHandler;

    public SensorService(final MessageService messageService, final ResponseHandler responseHandler){
        this.messageService = messageService;
        this.responseHandler = responseHandler;
    }

    public Optional<SensorData> addValueToSensor(final double value, final String sensorId){
        final SensorData sensorData = new SensorData(sensorId, value);
        final Optional<String> confirmationId = messageService.publish(sensorData);

        final Optional<String> confirmedValue = messageService.consume(confirmationId);
        if(confirmedValue.isPresent()){
            logger.debug("Successfully added value " + value + " to sensor " + sensorId);
            return responseHandler.handleResponse(confirmedValue.get(), SensorAddValueResponse.class, SensorData.class);
        }

        logger.debug("Failed to add sensor value " + value + " for sensor " + sensorId);
        return Optional.empty();
    }
}
