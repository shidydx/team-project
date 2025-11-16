package interface_adapter.left_news_summary;

public class LeftNewsSummaryViewModel {
    LeftNewsSummaryState state;

    public LeftNewsSummaryViewModel() {
        state = new LeftNewsSummaryState();
    }

    public LeftNewsSummaryState getState() {
        return state;
    }

    public void setState(LeftNewsSummaryState state) {
        this.state = state;
    }
}

