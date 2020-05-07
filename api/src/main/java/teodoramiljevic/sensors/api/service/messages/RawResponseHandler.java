package teodoramiljevic.sensors.api.service.messages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.api.exception.NotFoundException;
import teodoramiljevic.sensors.api.wrapper.ObjectMapperWrapper;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.ResponseBase;

import java.util.Optional;

import static teodoramiljevic.sensors.messaging.MessageStatus.NOT_FOUND;
import static teodoramiljevic.sensors.messaging.MessageStatus.SUCCESS;

@Service
public class RawResponseHandler implements ResponseHandler {

    private final Logger logger = LogManager.getLogger(RawResponseHandler.class);

    private final ModelMapper modelMapper;
    private final ObjectMapperWrapper objectMapper;

    public RawResponseHandler(final ModelMapper modelMapper, final ObjectMapperWrapper objectMapper) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public <T,C extends ResponseBase> Optional<T> handleResponse(final String response,
                                                                 final Class<C> responseClass,
                                                                 final Class<T> conversionClass) {
        final Optional<C> sensorDataAddValueResponse = deserializeValue(response, responseClass);
        if(sensorDataAddValueResponse.isPresent()){
            return handleResponseBasedOnStatus(sensorDataAddValueResponse.get(), conversionClass);
        }

        logger.debug("Message conversion to "+ responseClass + " failed", response);
        return Optional.empty();
    }

    private <T,C extends ResponseBase> Optional<T> handleResponseBasedOnStatus(final C response,
                                                                               final Class<T> conversionClass) {
        final MessageStatus status = response.getStatus();
        if(status == SUCCESS){
            return handleSuccess(response, conversionClass);
        }

        handleFailure(response);
        return Optional.empty();
    }

    private <T,C extends ResponseBase>  Optional<T>  handleSuccess(final C response, final Class<T> conversionClass){
        logger.debug("Message successfully received");
        return Optional.of(this.modelMapper.map(response, conversionClass));
    }

    // TODO: Handle other error types
    private void handleFailure(final ResponseBase response) {
        final MessageStatus status = response.getStatus();
        logger.debug("Failed to handle response with status " + status.name());

        if(response.getStatus() == NOT_FOUND){
            throw new NotFoundException(response.getMessageKey());
        }
    }

    private <T> Optional<T> deserializeValue(final String value, final Class<T> originalClass){
        return objectMapper.readValue(value, originalClass);
    }
}
