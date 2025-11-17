package interface_adapter.left_news_summary;

import use_case.left_news_summary.LeftNewsSummaryOutputBoundary;
import use_case.left_news_summary.LeftNewsSummaryOutputData;

public class LeftNewsSummaryPresenter implements LeftNewsSummaryOutputBoundary{
    private final LeftNewsSummaryViewModel viewModel;

    public LeftNewsSummaryPresenter(LeftNewsSummaryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void successView(LeftNewsSummaryOutputData outputData) {
        LeftNewsSummaryState currState = viewModel.getState();
        currState.setSummary(outputData.getSummary());
        currState.setErrorMessage("");
        viewModel.setState(currState);
    }

    @Override
    public void failureView(String errorMessage) {
        LeftNewsSummaryState currState = viewModel.getState();
        currState.setErrorMessage(errorMessage);
        currState.setSummary("");
        viewModel.setState(currState);
    }
}

