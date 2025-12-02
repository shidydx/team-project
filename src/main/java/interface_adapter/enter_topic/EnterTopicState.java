package interface_adapter.enter_topic;

public class EnterTopicState {
    private String topic;
    private String errorMsg;

    public EnterTopicState(EnterTopicState copy) {
        this.topic = copy.topic;
        this.errorMsg = copy.errorMsg;
    }

    public EnterTopicState() {}

    public String getTopic() {
        return topic;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMsg = errorMessage;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
