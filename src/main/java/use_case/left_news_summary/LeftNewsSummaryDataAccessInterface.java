package use_case.left_news_summary;

import entity.Article;
import java.util.List;

public interface LeftNewsSummaryDataAccessInterface {
    List<Article> fetchLeftNewsArticles(String topic);
    String summarizeLeftNewsArticles(List<Article> articles);
}

