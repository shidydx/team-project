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
            
            // Check for articles first before trying to summarize
            if (articles == null || articles.isEmpty()) {
                outputBoundary.failureView("No articles found for topic: " + topic);
                return;
            }
            
            String summary = dataAccess.summarizeLeftNewsArticles(articles);
            
            if (summary == null || summary.isEmpty()) {
                outputBoundary.failureView("Failed to generate summary");
                return;
            }
            
            LeftNewsSummaryOutputData outputData = new LeftNewsSummaryOutputData(summary, "");
            outputBoundary.successView(outputData);
            
        } catch (Exception e) {
            outputBoundary.failureView("Error: " + e.getMessage());
        }
    }
}

