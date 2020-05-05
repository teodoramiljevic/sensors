package teodoramiljevic.sensors.api.service.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;

import java.util.Optional;

import static teodoramiljevic.sensors.messaging.MessageStatus.SUCCESS;

@Service
public class RawResponseHandler implements ResponseHandler {

    private final Logger logger = LogManager.getLogger(RawResponseHandler.class);

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public RawResponseHandler(final ModelMapper modelMapper, final ObjectMapper objectMapper) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public <T,C extends ResponseBase> Optional<T> handleResponse(final String response, final Class<C> responseClass, final Class<T> conversionClass){
        final Optional<C> sensorDataAddValueResponse = deserializeValue(response, responseClass);
        if(sensorDataAddValueResponse.isPresent()){
            return handleResponseBasedOnStatus(sensorDataAddValueResponse.get(), conversionClass);
        }

        logger.debug("Message conversion to "+ responseClass + " failed", response);
        return Optional.empty();
    }

    private <T,C extends ResponseBase>  Optional<T> handleResponseBasedOnStatus(final C response, final Class<T> conversionClass){
        final MessageStatus status = response.getStatus();
        if(status == SUCCESS){
            return handleSuccess(response, conversionClass);
        }

        logger.debug("Failed to handle response with status " + status.name());
        return Optional.empty();
    }

    private <T,C extends ResponseBase>  Optional<T>  handleSuccess(final C response, final Class<T> conversionClass){
        logger.debug("Message successfully received");
        return Optional.of(this.modelMapper.map(response, conversionClass));
    }

    private <T> Optional<T> deserializeValue(final String value, final Class<T> originalClass){
        try{
            return Optional.of(objectMapper.readValue(value, originalClass));
        }catch(final Exception ex){
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}
