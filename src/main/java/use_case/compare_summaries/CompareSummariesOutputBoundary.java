package use_case.compare_summaries;

public interface CompareSummariesOutputBoundary {
    void successView(CompareSummariesOutputData outputData);
    void failureView(String errorMessage);
}

