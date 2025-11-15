package use_case.left_news_summary;

public interface LeftNewsSummaryOutputBoundary {
    void successView(LeftNewsSummaryOutputData outputData);
    void failureView(String errorMessage);
}

