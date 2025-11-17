package use_case.autosave_search_history;

public interface SaveTopicOutputBoundary {

    void prepareSuccessView(SaveTopicOutputData outputData);

    void prepareFailView(String errorMessage);
}
