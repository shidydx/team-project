package use_case.comparison;

import entity.Article;
import java.util.List;

public class ComparisonOutputData {
    private final String topic;
    private final String leftSummary;
    private final String rightSummary;
    private final String comparisonAnalysis;
    private final List<Article> leftArticles;
    private final List<Article> rightArticles;

    public ComparisonOutputData(String topic, String leftSummary, String rightSummary, 
                                String comparisonAnalysis, 
                                List<Article> leftArticles, 
                                List<Article> rightArticles) {
        this.topic = topic;
        this.leftSummary = leftSummary;
        this.rightSummary = rightSummary;
        this.comparisonAnalysis = comparisonAnalysis;
        this.leftArticles = leftArticles;
        this.rightArticles = rightArticles;
    }

    public String getTopic() {
        return topic;
    }

    public String getLeftSummary() {
        return leftSummary;
    }

    public String getRightSummary() {
        return rightSummary;
    }

    public String getComparisonAnalysis() {
        return comparisonAnalysis;
    }

    public List<Article> getLeftArticles() {
        return leftArticles;
    }

    public List<Article> getRightArticles() {
        return rightArticles;
    }
}

