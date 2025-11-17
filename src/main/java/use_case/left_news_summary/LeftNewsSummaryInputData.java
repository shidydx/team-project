package use_case.left_news_summary;

public class LeftNewsSummaryInputData {
    private final String topic;

    public LeftNewsSummaryInputData(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}

