package use_case.loadsearch;

public interface LoadSearchHistoryOutputBoundary {

    void prepareSuccessView(LoadSearchHistoryOutputData outputData);

    void prepareFailView(String errorMessage);
}
