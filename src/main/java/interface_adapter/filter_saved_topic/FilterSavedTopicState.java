package interface_adapter.filter_saved_topic;

public class FilterSavedTopicState {
    private String topic;
    private String errorMsg;

    public FilterSavedTopicState(FilterSavedTopicState copy) {
        this.topic = copy.topic;
        this.errorMsg = copy.errorMsg;
    }

    public FilterSavedTopicState() {}

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



