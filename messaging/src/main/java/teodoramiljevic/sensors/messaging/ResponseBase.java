package teodoramiljevic.sensors.messaging;

public class ResponseBase {
    private MessageStatus status;

    public ResponseBase(){

    }

    public ResponseBase(final MessageStatus status){
        this.status = status;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(final MessageStatus status) {
        this.status = status;
    }
}
