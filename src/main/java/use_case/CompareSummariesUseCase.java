package use_case;

import use_case.compare_summaries.CompareSummariesInputBoundary;
import use_case.compare_summaries.CompareSummariesInputData;

public class CompareSummariesUseCase {
    private final CompareSummariesInputBoundary interactor;

    public CompareSummariesUseCase(CompareSummariesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String leftSummary, String rightSummary, String topic) {
        CompareSummariesInputData inputData = new CompareSummariesInputData(leftSummary, rightSummary, topic);
        interactor.execute(inputData);
    }
}
