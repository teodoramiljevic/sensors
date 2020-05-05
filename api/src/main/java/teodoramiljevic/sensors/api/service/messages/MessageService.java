package teodoramiljevic.sensors.api.service.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.communication.MessageConsumer;
import teodoramiljevic.sensors.api.communication.MessagePublisher;

import java.util.Optional;

@Service
public class MessageService implements Publisher, Consumer {

    private final MessagePublisher messagePublisher;
    private final MessageConsumer messageConsumer;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(MessageService.class);

    public MessageService(final MessagePublisher messagePublisher,
                          final MessageConsumer messageConsumer,
                          final ObjectMapper objectMapper) {

        this.messagePublisher = messagePublisher;
        this.messageConsumer = messageConsumer;
        this.objectMapper = objectMapper;
    }

    public <T> Optional<String> publish(final T data){
        try{
            final String sensorDataJsonValue =  objectMapper.writeValueAsString(data);
            return messagePublisher.publish(sensorDataJsonValue, data.getClass().getName());
        }
        catch(final Exception ex){
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    public Optional<String> consume(final Optional<String> consumptionId){
        if(consumptionId.isPresent()){
            return messageConsumer.consume(consumptionId.get());
        }

        logger.debug("Consuming failed, consumption  ID was not present");
        return Optional.empty();
    }

}
