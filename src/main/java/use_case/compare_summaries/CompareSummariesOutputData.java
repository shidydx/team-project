package use_case.compare_summaries;

public class CompareSummariesOutputData {
    private final String leftSummary;
    private final String rightSummary;
    private final String comparisonAnalysis;
    private final String errorMessage;

    public CompareSummariesOutputData(String leftSummary, String rightSummary, String comparisonAnalysis, String errorMessage) {
        this.leftSummary = leftSummary;
        this.rightSummary = rightSummary;
        this.comparisonAnalysis = comparisonAnalysis;
        this.errorMessage = errorMessage;
    }

    public String getLeftSummary() {
        return leftSummary;
    }

    public String getRightSummary() {
        return rightSummary;
    }

    public String getComparisonAnalysis() {
        return comparisonAnalysis;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

