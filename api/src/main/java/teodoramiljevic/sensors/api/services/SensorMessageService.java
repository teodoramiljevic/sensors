package teodoramiljevic.sensors.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import teodoramiljevic.sensors.api.services.message.MessageConsumer;
import teodoramiljevic.sensors.api.services.message.MessagePublisher;

import java.util.Optional;

class SensorMessageService {

    private final MessagePublisher messagePublisher;
    private final MessageConsumer messageConsumer;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(SensorMessageService.class);

    SensorMessageService(final MessagePublisher messagePublisher, final MessageConsumer messageConsumer,
                         final ObjectMapper objectMapper,
                            final ModelMapper modelMapper) {
        this.messagePublisher = messagePublisher;
        this.messageConsumer = messageConsumer;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    <T> Optional<T> deserializeValue(final String value, final Class<T> originalClass){
        try{
            return Optional.of(objectMapper.readValue(value, originalClass));
        }catch(final Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            return Optional.empty();
        }
    }

    <T> Optional<String> publishValue(final T sensorData){
        try{
            final String sensorDataJsonValue =  objectMapper.writeValueAsString(sensorData);
            return messagePublisher.publish(sensorDataJsonValue);
        }
        catch(final Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            return Optional.empty();
        }
    }

    Optional<String> getValue(final Optional<String> confirmationId){
        if(confirmationId.isPresent()){
            return messageConsumer.consume(confirmationId.get());
        }

        logger.debug("Confirming thar value is added failed, publishing did not return valid ID for confirmation");
        return Optional.empty();
    }

}
