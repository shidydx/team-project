package use_case.delete_saved_topic;

public interface DeleteSavedTopicOutputBoundary {
    void successView(DeleteSavedTopicOutputData outputData);
    void failureView(String errorMsg);
}

