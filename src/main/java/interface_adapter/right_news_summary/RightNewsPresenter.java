package interface_adapter.right_news_summary;

import use_case.right_news_summary.RightNewsSummaryOutputBoundary;
import use_case.right_news_summary.RightNewsSummaryOutputData;

public class RightNewsPresenter implements RightNewsSummaryOutputBoundary {
    private final RightNewsViewModel viewModel;

    public RightNewsPresenter(RightNewsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void summarizeSuccessView(RightNewsSummaryOutputData summarizeRightNewsOutputData) {
        viewModel.setSummary(summarizeRightNewsOutputData.getSummary());
        viewModel.setUrl(summarizeRightNewsOutputData.getUrl());
        viewModel.setTitle(summarizeRightNewsOutputData.getTitle());
        viewModel.setName(summarizeRightNewsOutputData.getSourceName());
        viewModel.setArticles(summarizeRightNewsOutputData.getArticles());
        viewModel.setErrorMessage("");
    }

    @Override
    public void summarizeFailView(String failedMessage) {
        viewModel.setSummary("");
        viewModel.setUrl("");
        viewModel.setTitle("");
        viewModel.setName("");
        viewModel.setArticles(null);
        viewModel.setErrorMessage(failedMessage);
    }
}