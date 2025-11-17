package use_case.compare_summaries;

public class CompareSummariesInteractor implements CompareSummariesInputBoundary {
    private final CompareSummariesOutputBoundary outputBoundary;
    private final CompareSummariesDataAccessInterface dataAccess;

    public CompareSummariesInteractor(CompareSummariesOutputBoundary outputBoundary, CompareSummariesDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(CompareSummariesInputData inputData) {
        String leftSummary = inputData.getLeftSummary();
        String rightSummary = inputData.getRightSummary();
        String topic = inputData.getTopic();

        try {
            if (leftSummary == null || leftSummary.isEmpty()) {
                outputBoundary.failureView("Left summary is missing");
                return;
            }

            if (rightSummary == null || rightSummary.isEmpty()) {
                outputBoundary.failureView("Right summary is missing");
                return;
            }

            String comparisonAnalysis = dataAccess.generateComparisonAnalysis(leftSummary, rightSummary, topic);

            if (comparisonAnalysis == null || comparisonAnalysis.isEmpty()) {
                outputBoundary.failureView("Failed to generate comparison analysis");
                return;
            }

            CompareSummariesOutputData outputData = new CompareSummariesOutputData(
                leftSummary, rightSummary, comparisonAnalysis, "");
            outputBoundary.successView(outputData);

        } catch (Exception e) {
            outputBoundary.failureView("Error: " + e.getMessage());
        }
    }
}

