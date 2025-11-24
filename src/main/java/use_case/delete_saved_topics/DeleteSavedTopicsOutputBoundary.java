package use_case.delete_saved_topics;

public interface DeleteSavedTopicsOutputBoundary {
    void successView(DeleteSavedTopicsOutputData outputData);
    void failureView(String errorMessage);
}
