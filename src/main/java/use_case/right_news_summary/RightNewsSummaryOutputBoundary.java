package use_case.right_news_summary;

public interface RightNewsSummaryOutputBoundary {
    void summarizeSuccessView(RightNewsSummaryOutputData summarizeRightNewsOutputData);

    void summarizeFailView(String failedMessage);
}
