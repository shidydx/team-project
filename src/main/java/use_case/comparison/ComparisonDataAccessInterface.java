package use_case.comparison;

import entity.Article;
import java.util.List;

public interface ComparisonDataAccessInterface {
    List<Article> fetchLeftNewsArticles(String topic);
    List<Article> fetchRightNewsArticles(String topic);
    String summarizeArticles(List<Article> articles);
    String generateComparison(String leftSummary, String rightSummary);
}




