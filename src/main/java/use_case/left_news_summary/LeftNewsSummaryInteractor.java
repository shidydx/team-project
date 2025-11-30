package use_case.left_news_summary;

import entity.Article;
import java.util.List;

public class LeftNewsSummaryInteractor implements LeftNewsSummaryInputBoundary {
    private final LeftNewsSummaryOutputBoundary outputBoundary;
    private final LeftNewsSummaryDataAccessInterface dataAccess;

    public LeftNewsSummaryInteractor(LeftNewsSummaryOutputBoundary outputBoundary, LeftNewsSummaryDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(LeftNewsSummaryInputData inputData) {
        String topic = inputData.getTopic();
        try {
            List<Article> articles = dataAccess.fetchLeftNewsArticles(topic);
            String summary = dataAccess.summarizeLeftNewsArticles(articles);
            
            if (articles == null || articles.isEmpty()) {
                outputBoundary.failureView("No articles found for topic: " + topic);
                return;
            }
            
            if (summary == null || summary.isEmpty()) {
                outputBoundary.failureView("Failed to generate summary");
                return;
            }
            
            LeftNewsSummaryOutputData outputData = new LeftNewsSummaryOutputData(summary, "", articles);
            outputBoundary.successView(outputData);
            
        } catch (Exception e) {
            outputBoundary.failureView("Error: " + e.getMessage());
        }
    }
}

