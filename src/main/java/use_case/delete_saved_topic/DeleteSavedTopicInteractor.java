package use_case.delete_saved_topic;

import entity.Topic;

public class DeleteSavedTopicInteractor implements DeleteSavedTopicInputBoundary {
    private final DeleteSavedTopicOutputBoundary outputBoundary;
    private final DeleteSavedTopicDataAccessInterface dataAccess;

    public DeleteSavedTopicInteractor(DeleteSavedTopicOutputBoundary outputBoundary, 
                                     DeleteSavedTopicDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(DeleteSavedTopicInputData inputData) {
        String topic_input = inputData.getTopic();
        if (topic_input.isEmpty()) {
            outputBoundary.failureView("Topic input is empty. Please try again.");
        } else {
            try {
                Topic topicDeleted = dataAccess.deleteTopic(topic_input);

                if (topicDeleted == null) {
                    outputBoundary.failureView("No topic named " + topic_input + " was found in saved topics.");
                    return;
                } else {
                    String successMsg = "Topic " + topic_input + " was successfully deleted.";
                    DeleteSavedTopicOutputData outputData = new DeleteSavedTopicOutputData(successMsg, "");
                    outputBoundary.successView(outputData);
                }

            } catch (Exception e) {
                outputBoundary.failureView("Error: " + e.getMessage());
            }
        }
    }
}
