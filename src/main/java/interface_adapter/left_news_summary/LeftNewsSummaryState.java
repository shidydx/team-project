package interface_adapter.left_news_summary;

public class LeftNewsSummaryState {
    private String summary = "";
    private String errorMessage = "";

    public LeftNewsSummaryState(LeftNewsSummaryState copy) {
        summary = copy.summary;
        errorMessage = copy.errorMessage;
    }

    public LeftNewsSummaryState() {}

    public String getSummary() {
        return summary;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

