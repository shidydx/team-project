package use_case.compare_summaries;

public class CompareSummariesInputData {
    private final String leftSummary;
    private final String rightSummary;
    private final String topic;

    public CompareSummariesInputData(String leftSummary, String rightSummary, String topic) {
        this.leftSummary = leftSummary;
        this.rightSummary = rightSummary;
        this.topic = topic;
    }

    public String getLeftSummary() {
        return leftSummary;
    }

    public String getRightSummary() {
        return rightSummary;
    }

    public String getTopic() {
        return topic;
    }
}

