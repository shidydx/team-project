package data_access;

import entity.Article;
import use_case.NewsFetcherService;
import use_case.right_news_summary.RightNewsSummaryDataAccessInterface;

import java.util.List;

public class RightNewsSummaryDataAccessImpl implements RightNewsSummaryDataAccessInterface {
    private final NewsFetcherService newsFetcherService;

    public RightNewsSummaryDataAccessImpl(NewsFetcherService newsFetcherService) {
        this.newsFetcherService = newsFetcherService;
    }

    @Override
    public List<Article> fetchRightNewsArticles(String topic) {
        return newsFetcherService.fetchNews(topic, "right");
    }
}
