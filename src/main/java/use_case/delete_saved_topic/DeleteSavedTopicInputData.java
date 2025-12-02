package use_case.delete_saved_topic;

public class DeleteSavedTopicInputData {
    private final String topic;

    public DeleteSavedTopicInputData(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}



