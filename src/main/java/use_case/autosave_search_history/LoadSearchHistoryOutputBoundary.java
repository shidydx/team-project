package use_case.autosave_search_history;

public interface LoadSearchHistoryOutputBoundary {

    /** Called when history exists (may still be empty list). */
    void prepareSuccessView(LoadSearchHistoryOutputData outputData);

    /** Called when there is some unexpected failure (DAO etc.). */
    void prepareFailView(String errorMessage);
}
