package interface_adapter.comparison;

import entity.Article;
import java.util.ArrayList;
import java.util.List;

public class ComparisonState {
    private String topic;
    private String leftSummary;
    private String rightSummary;
    private String comparisonAnalysis;
    private List<Article> leftArticles;
    private List<Article> rightArticles;
    private String error;
    private boolean isLoading;

    public ComparisonState() {
        this.topic = "";
        this.leftSummary = "";
        this.rightSummary = "";
        this.comparisonAnalysis = "";
        this.leftArticles = new ArrayList<>();
        this.rightArticles = new ArrayList<>();
        this.error = "";
        this.isLoading = false;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLeftSummary() {
        return leftSummary;
    }

    public void setLeftSummary(String leftSummary) {
        this.leftSummary = leftSummary;
    }

    public String getRightSummary() {
        return rightSummary;
    }

    public void setRightSummary(String rightSummary) {
        this.rightSummary = rightSummary;
    }

    public String getComparisonAnalysis() {
        return comparisonAnalysis;
    }

    public void setComparisonAnalysis(String comparisonAnalysis) {
        this.comparisonAnalysis = comparisonAnalysis;
    }

    public List<Article> getLeftArticles() {
        return leftArticles;
    }

    public void setLeftArticles(List<Article> leftArticles) {
        this.leftArticles = leftArticles;
    }

    public List<Article> getRightArticles() {
        return rightArticles;
    }

    public void setRightArticles(List<Article> rightArticles) {
        this.rightArticles = rightArticles;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}




