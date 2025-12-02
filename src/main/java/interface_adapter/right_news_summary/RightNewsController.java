package interface_adapter.right_news_summary;

import use_case.right_news_summary.RightNewsSummaryInputBoundary;
import use_case.right_news_summary.RightNewsSummaryInputData;

public class RightNewsController {
    private final RightNewsSummaryInputBoundary rightNewsInputBoundary;
    public RightNewsController(RightNewsSummaryInputBoundary inputBoundary){this.rightNewsInputBoundary = inputBoundary;}

    public void execute(String keyword){
        final RightNewsSummaryInputData rightNewsInputData = new RightNewsSummaryInputData(keyword);
        rightNewsInputBoundary.execute(rightNewsInputData);
    }
}
