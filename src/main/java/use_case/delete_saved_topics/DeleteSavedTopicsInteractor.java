package use_case.delete_saved_topics;

import entity.Topic;

public class DeleteSavedTopicsInteractor implements DeleteSavedTopicsInputBoundary {
    private final DeleteSavedTopicsOutputBoundary outputBoundary;
    private final DeleteSavedTopicsDataAccessInterface dataAccess;

    public DeleteSavedTopicsInteractor(DeleteSavedTopicsOutputBoundary outputBoundary, DeleteSavedTopicsDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(DeleteSavedTopicsInputData inputData) {
        String topic_input = inputData.getTopic();
        try {
            Topic topicDeleted = dataAccess.deleteTopic(topic_input);

            if (topicDeleted == null) {
                outputBoundary.failureView("No topic named " + topic_input + " was found.");
            }

            String successMsg = "Topic named " + topic_input + " was deleted successfully";
            DeleteSavedTopicsOutputData outputData = new DeleteSavedTopicsOutputData(successMsg, "");
            outputBoundary.successView(outputData);

        } catch (Exception e) {
            outputBoundary.failureView("Error: " + e.getMessage());
        }
    }
}
