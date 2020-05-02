package teodoramiljevic.sensors.api.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.models.SensorData;
import teodoramiljevic.sensors.api.models.SensorDataList;
import teodoramiljevic.sensors.api.services.messages.MessageService;
import teodoramiljevic.sensors.api.services.messages.ResponseHandler;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueRequest;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueResponse;
import teodoramiljevic.sensors.messaging.SensorGetLatest.SensorGetLatestRequest;
import teodoramiljevic.sensors.messaging.SensorGetLatest.SensorGetLatestResponse;
import teodoramiljevic.sensors.messaging.SensorGetValues.SensorGetValuesRequest;
import teodoramiljevic.sensors.messaging.SensorGetValues.SensorsGetValuesResponse;

import java.util.Optional;

@Service
public class SensorService{
    private static final Logger logger = LogManager.getLogger(SensorService.class);
    private final MessageService messageService;
    private final ResponseHandler responseHandler;
    private final ModelMapper modelMapper;

    public SensorService(final MessageService messageService, final ResponseHandler responseHandler, final ModelMapper modelMapper){
        this.messageService = messageService;
        this.responseHandler = responseHandler;
        this.modelMapper = modelMapper;
    }

    public Optional<SensorData> addValueToSensor(final String sensorId, final SensorData data){
        final SensorAddValueRequest sensorAddValueRequest = new SensorAddValueRequest(sensorId, modelMapper.map(data.getValue(), teodoramiljevic.sensors.messaging.SensorValue.class));
        final Optional<String> confirmationId = messageService.publish(sensorAddValueRequest);

        final Optional<String> confirmedValue = messageService.consume(confirmationId);
        if(confirmedValue.isPresent()){
            return responseHandler.handleResponse(confirmedValue.get(), SensorAddValueResponse.class, SensorData.class);
        }

        logger.debug("Failed to add sensor value " + data.getValue().getValue() + " for sensor " + sensorId);
        return Optional.empty();
    }

    public Optional<SensorData> getLatest(final String sensorId){
        final SensorGetLatestRequest sensorGetLatestRequest = new SensorGetLatestRequest();
        sensorGetLatestRequest.setSensorId(sensorId);

        final Optional<String> confirmationId = messageService.publish(sensorGetLatestRequest);

        final Optional<String> confirmedValue = messageService.consume(confirmationId);
        if(confirmedValue.isPresent()){
            return responseHandler.handleResponse(confirmedValue.get(), SensorGetLatestResponse.class, SensorData.class);
        }

        logger.debug("Failed to get latest value for sensor " + sensorId);
        return Optional.empty();
    }

    public SensorDataList getValues(final String sensorId){
        final SensorGetValuesRequest sensorGetValuesRequest = new SensorGetValuesRequest();
        sensorGetValuesRequest.setSensorId(sensorId);

        final Optional<String> confirmationId = messageService.publish(sensorGetValuesRequest);

        final Optional<String> confirmedValue = messageService.consume(confirmationId);
        if(confirmedValue.isPresent()){
            final Optional<SensorDataList> sensorDataList = responseHandler.handleResponse(confirmedValue.get(), SensorsGetValuesResponse.class, SensorDataList.class);
            return sensorDataList.get();
        }

        return new SensorDataList();
    }
}
