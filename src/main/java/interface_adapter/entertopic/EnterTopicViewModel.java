package interface_adapter.entertopic;

public class EnterTopicViewModel {
    private String errorMessage;
    private String topic;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTopic() {
        return topic;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
