package teodoramiljevic.sensors.api.service.messages;


import teodoramiljevic.sensors.messaging.ResponseBase;

import java.util.Optional;

public interface ResponseHandler {
    <T,C extends ResponseBase> Optional<T> handleResponse(final String response, final Class<C> responseClass, final Class<T> conversionClass);
}
