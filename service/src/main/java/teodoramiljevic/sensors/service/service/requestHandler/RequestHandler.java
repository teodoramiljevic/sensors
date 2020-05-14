package teodoramiljevic.sensors.service.service.requestHandler;

/**
 * Describes actions that can be made upon request received from a queue
 * @param <T> Request type
 * @param <C> Response type
 */
public abstract class RequestHandler<T, C> {

    public abstract C handle(T request);

    /**
     * Each handler can support one request type.
     * The implementation could be adjusted to more request types with chain of responsibility.
     */
    private final Class acceptedRequestType;

    public RequestHandler(final Class acceptedRequestType) {
        this.acceptedRequestType = acceptedRequestType;
    }

    public Class getAcceptedRequestType() {
        return acceptedRequestType;
    }
}
