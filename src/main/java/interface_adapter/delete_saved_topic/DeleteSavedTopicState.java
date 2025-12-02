package interface_adapter.delete_saved_topic;

public class DeleteSavedTopicState {
    private String topic;
    private String errorMsg;

    public DeleteSavedTopicState(DeleteSavedTopicState copy) {
        this.topic = copy.topic;
        this.errorMsg = copy.errorMsg;
    }

    public DeleteSavedTopicState() {}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

