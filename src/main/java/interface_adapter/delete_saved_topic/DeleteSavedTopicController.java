package interface_adapter.delete_saved_topic;

import use_case.delete_saved_topic.DeleteSavedTopicInputBoundary;
import use_case.delete_saved_topic.DeleteSavedTopicInputData;

public class DeleteSavedTopicController {
    private final DeleteSavedTopicInputBoundary inputBoundary;

    public DeleteSavedTopicController(DeleteSavedTopicInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(String topic) {
        DeleteSavedTopicInputData inputData = new DeleteSavedTopicInputData(topic);
        inputBoundary.execute(inputData);
    }
}



