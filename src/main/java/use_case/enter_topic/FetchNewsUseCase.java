package use_case.enter_topic;

import entity.Article;
import use_case.NewsFetcherService;

import java.util.List;

/**
 * validate the keyword entered by the user and fetch relevant news articles
 */
public class FetchNewsUseCase {
    private final NewsFetcherService newsFetcher;

    public FetchNewsUseCase(NewsFetcherService newsFetcher) {
        this.newsFetcher = newsFetcher;
    }
    public boolean validateTopic (String keyword) {
        if (keyword.equals("")) {
            return false;
        } else {
            List<Article> leftArticles = newsFetcher.fetchNews(keyword, "left");
            List<Article> rightArticles = newsFetcher.fetchNews(keyword, "right");
            return leftArticles.size() > 0 && rightArticles.size() > 0;
        }
    }
}
