package use_case.delete_saved_topics;

public class DeleteSavedTopicsInputData {
    private final String topic;

    public DeleteSavedTopicsInputData(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
