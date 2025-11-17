package use_case.left_news_summary;

public class LeftNewsSummaryOutputData {
    private final String summary;
    private final String errorMessage;

    public LeftNewsSummaryOutputData(String summary, String errorMessage) {
        this.summary = summary;
        this.errorMessage = errorMessage;
    }

    public String getSummary() {
        return summary;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

