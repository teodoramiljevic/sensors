package teodoramiljevic.sensors.service.service.requestHandler;

public abstract class RequestHandler<T, C> {

    public abstract C handle(T request);

    private final Class acceptedRequestType;

    public RequestHandler(final Class acceptedRequestType) {
        this.acceptedRequestType = acceptedRequestType;
    }

    public Class getAcceptedRequestType() {
        return acceptedRequestType;
    }
}
