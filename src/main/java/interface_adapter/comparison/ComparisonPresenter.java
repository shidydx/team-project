package interface_adapter.comparison;

import use_case.comparison.ComparisonOutputBoundary;
import use_case.comparison.ComparisonOutputData;

public class ComparisonPresenter implements ComparisonOutputBoundary {
    private final ComparisonViewModel viewModel;

    public ComparisonPresenter(ComparisonViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void successView(ComparisonOutputData outputData) {
        ComparisonState state = viewModel.getState();
        state.setTopic(outputData.getTopic());
        state.setLeftSummary(outputData.getLeftSummary());
        state.setRightSummary(outputData.getRightSummary());
        state.setComparisonAnalysis(outputData.getComparisonAnalysis());
        state.setLeftArticles(outputData.getLeftArticles());
        state.setRightArticles(outputData.getRightArticles());
        state.setError("");
        state.setLoading(false);
        viewModel.firePropertyChanged();
    }

    @Override
    public void failureView(String error) {
        ComparisonState state = viewModel.getState();
        state.setError(error);
        state.setLoading(false);
        viewModel.firePropertyChanged();
    }
}

