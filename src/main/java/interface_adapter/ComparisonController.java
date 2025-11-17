package interface_adapter;

import use_case.compare_summaries.CompareSummariesInputBoundary;
import use_case.compare_summaries.CompareSummariesInputData;

public class ComparisonController {
    private final CompareSummariesInputBoundary inputBoundary;

    public ComparisonController(CompareSummariesInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(String leftSummary, String rightSummary, String topic) {
        CompareSummariesInputData inputData = new CompareSummariesInputData(leftSummary, rightSummary, topic);
        inputBoundary.execute(inputData);
    }
}

