package teodoramiljevic.sensors.messaging;

public class ResponseBase {
    private MessageStatus status;
    private String messageKey;

    public ResponseBase(){
    }

    public ResponseBase(final MessageStatus status, final String messageKey){
        this.status = status;
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(final MessageStatus status) {
        this.status = status;
    }
}
