package teodoramiljevic.sensors.service.service.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RequestHandlerFactory {

    private final Logger logger = LogManager.getLogger(RequestHandlerFactory.class);

    private final Map<Class, RequestHandler> requestHandlers = new HashMap<>();

    public RequestHandlerFactory(final List<RequestHandler> requestHandlers) {
        requestHandlers.forEach(requestHandler -> this.requestHandlers.put(requestHandler.getAcceptedRequestType(), requestHandler));
    }

    public Optional<RequestHandler> getHandler(final Class requestClass) {
        final RequestHandler handler = this.requestHandlers.getOrDefault(requestClass, null); // TODO: Maybe add some default handler

        if (handler == null) {
            logger.debug("Handler was not found");
        }

        return Optional.ofNullable(handler);
    }
}
