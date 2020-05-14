package teodoramiljevic.sensors.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.model.SensorData;
import teodoramiljevic.sensors.api.model.SensorDataList;
import teodoramiljevic.sensors.api.model.SensorValue;
import teodoramiljevic.sensors.api.repository.SensorCacheRepository;
import teodoramiljevic.sensors.api.service.messages.MessageService;
import teodoramiljevic.sensors.api.service.messages.ResponseHandler;
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
    private final SensorCacheRepository sensorCacheRepository;

    public SensorService(final MessageService messageService,
                         final ResponseHandler responseHandler,
                         final ModelMapper modelMapper,
                         final SensorCacheRepository sensorCacheRepository){

        this.messageService = messageService;
        this.responseHandler = responseHandler;
        this.modelMapper = modelMapper;
        this.sensorCacheRepository = sensorCacheRepository;
    }

    public Optional<SensorData> addValue(final String sensorId, final SensorData data){
        final SensorAddValueRequest sensorAddValueRequest = new SensorAddValueRequest(sensorId, modelMapper.map(data.getValue(),
                                                                                        teodoramiljevic.sensors.messaging.SensorValue.class));

        final Optional<String> confirmationId = messageService.publish(sensorAddValueRequest);

        final Optional<String> confirmedValue = messageService.consume(confirmationId);
        if(confirmedValue.isPresent()){
            final Optional<SensorData> response = responseHandler.handleResponse(confirmedValue.get(), SensorAddValueResponse.class, SensorData.class);
            if(response.isPresent()){
                SensorValue value = response.get().getValue();
                setValueToCache(sensorId, value);
            }
            return response;
        }

        logger.error("Failed to add sensor value " + data.getValue().getValue() + " for sensor " + sensorId);
        return Optional.empty();
    }

    public Optional<SensorData> getLatest(final String sensorId){
        final Optional<SensorValue> sensorValue = sensorCacheRepository.getLatest(sensorId);
        if(sensorValue.isPresent()){
            SensorValue value = sensorValue.get();
            logger.info("Latest " + value + " taken from cache.");
            return Optional.of(new SensorData(value));
        }

        final SensorGetLatestRequest sensorGetLatestRequest = new SensorGetLatestRequest();
        sensorGetLatestRequest.setSensorId(sensorId);

        final Optional<String> confirmationId = messageService.publish(sensorGetLatestRequest);

        final Optional<String> confirmedValue = messageService.consume(confirmationId);
        if(confirmedValue.isPresent()){
            final Optional<SensorData> response = responseHandler.handleResponse(confirmedValue.get(), SensorGetLatestResponse.class, SensorData.class);
            if(response.isPresent()){
                SensorValue value = response.get().getValue();
                setValueToCache(sensorId, value);
            }
            return response;
        }

        logger.error("Failed to get latest value for sensor " + sensorId);
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

    private void setValueToCache(String sensorId, SensorValue value){
        logger.info("Setting latest "+value+" of sensor " + sensorId + " to cache.");
        sensorCacheRepository.setLatest(sensorId, value);
    }
}
