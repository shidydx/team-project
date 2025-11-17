package interface_adapter.compare_summaries;

public class ComparisonState {
    private String leftSummary = "";
    private String rightSummary = "";
    private String comparisonAnalysis = "";
    private String errorMessage = "";

    public ComparisonState(ComparisonState copy) {
        leftSummary = copy.leftSummary;
        rightSummary = copy.rightSummary;
        comparisonAnalysis = copy.comparisonAnalysis;
        errorMessage = copy.errorMessage;
    }

    public ComparisonState() {}

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

    public void setLeftSummary(String leftSummary) {
        this.leftSummary = leftSummary;
    }

    public void setRightSummary(String rightSummary) {
        this.rightSummary = rightSummary;
    }

    public void setComparisonAnalysis(String comparisonAnalysis) {
        this.comparisonAnalysis = comparisonAnalysis;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

