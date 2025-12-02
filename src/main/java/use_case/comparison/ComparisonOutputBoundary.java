package use_case.comparison;

public interface ComparisonOutputBoundary {
    void successView(ComparisonOutputData outputData);
    void failureView(String error);
}




