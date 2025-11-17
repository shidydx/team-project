package interface_adapter.compare_summaries;

import use_case.compare_summaries.CompareSummariesOutputBoundary;
import use_case.compare_summaries.CompareSummariesOutputData;
import javax.swing.SwingUtilities;

public class ComparisonPresenter implements CompareSummariesOutputBoundary {
    private final ComparisonViewModel viewModel;

    public ComparisonPresenter(ComparisonViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void successView(CompareSummariesOutputData outputData) {
        // Ensure state update happens on EDT
        SwingUtilities.invokeLater(() -> {
            ComparisonState oldState = viewModel.getState();
            // Create a new state object to ensure property change fires
            ComparisonState newState = new ComparisonState(oldState);
            newState.setLeftSummary(outputData.getLeftSummary());
            newState.setRightSummary(outputData.getRightSummary());
            String analysis = outputData.getComparisonAnalysis();
            System.out.println("Presenter: Setting comparison analysis. Length: " + 
                (analysis != null ? analysis.length() : 0));
            newState.setComparisonAnalysis(analysis);
            newState.setErrorMessage("");
            viewModel.setState(newState);
            System.out.println("Presenter: State updated, property change fired");
        });
    }

    @Override
    public void failureView(String errorMessage) {
        // Ensure state update happens on EDT
        SwingUtilities.invokeLater(() -> {
            ComparisonState currState = viewModel.getState();
            currState.setErrorMessage(errorMessage);
            currState.setLeftSummary("");
            currState.setRightSummary("");
            currState.setComparisonAnalysis("");
            viewModel.setState(currState);
        });
    }
}

