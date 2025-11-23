package use_case.right_news_summary;

import entity.Article;
import java.util.List;

public interface RightNewsSummaryDataAccessInterface {
    List<Article> fetchRightNewsArticles(String topic);
}