package use_case.comparison;

import entity.Article;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ComparisonInteractor implements ComparisonInputBoundary {
    private final ComparisonOutputBoundary outputBoundary;
    private final ComparisonDataAccessInterface dataAccess;

    public ComparisonInteractor(ComparisonOutputBoundary outputBoundary, 
                                ComparisonDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(ComparisonInputData inputData) {
        String topic = inputData.getTopic();
        
        if (topic == null || topic.trim().isEmpty()) {
            outputBoundary.failureView("Topic is empty");
            return;
        }

        try {
            CompletableFuture<List<Article>> leftFuture = CompletableFuture.supplyAsync(() ->
                dataAccess.fetchLeftNewsArticles(topic)
            );

            CompletableFuture<List<Article>> rightFuture = CompletableFuture.supplyAsync(() ->
                dataAccess.fetchRightNewsArticles(topic)
            );

            List<Article> leftArticles = leftFuture.join();
            List<Article> rightArticles = rightFuture.join();

            if (leftArticles == null || leftArticles.isEmpty()) {
                outputBoundary.failureView("No left-leaning articles found for topic: " + topic);
                return;
            }

            if (rightArticles == null || rightArticles.isEmpty()) {
                outputBoundary.failureView("No right-leaning articles found for topic: " + topic);
                return;
            }

            CompletableFuture<String> leftSummaryFuture = CompletableFuture.supplyAsync(() ->
                dataAccess.summarizeArticles(leftArticles)
            );

            CompletableFuture<String> rightSummaryFuture = CompletableFuture.supplyAsync(() ->
                dataAccess.summarizeArticles(rightArticles)
            );

            String leftSummary = leftSummaryFuture.join();
            String rightSummary = rightSummaryFuture.join();

            if (leftSummary == null || leftSummary.isEmpty()) {
                outputBoundary.failureView("Failed to generate left summary");
                return;
            }

            if (rightSummary == null || rightSummary.isEmpty()) {
                outputBoundary.failureView("Failed to generate right summary");
                return;
            }

            String comparisonAnalysis = dataAccess.generateComparison(leftSummary, rightSummary);

            if (comparisonAnalysis == null || comparisonAnalysis.isEmpty()) {
                outputBoundary.failureView("Failed to generate comparison analysis");
                return;
            }

            ComparisonOutputData outputData = new ComparisonOutputData(
                topic, leftSummary, rightSummary, comparisonAnalysis, leftArticles, rightArticles
            );
            outputBoundary.successView(outputData);

        } catch (Exception e) {
            outputBoundary.failureView("Error: " + e.getMessage());
        }
    }
}

