package interface_adapter.left_news_summary;

import use_case.left_news_summary.LeftNewsSummaryInputBoundary;
import use_case.left_news_summary.LeftNewsSummaryInputData;

public class LeftNewsSummaryController {

    private final LeftNewsSummaryInputBoundary inputBoundary;

    public LeftNewsSummaryController(LeftNewsSummaryInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(String topic) {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData(topic);
        inputBoundary.execute(inputData);
    }
}

