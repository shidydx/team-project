package data_access;

import entity.Article;
import use_case.NewsFetcherService;
import use_case.enter_topic.EnterTopicDataAccessInterface;

import java.util.List;

public class EnterTopicDataAccessImpl implements EnterTopicDataAccessInterface {
    private final NewsFetcherService newsFetcher;

    public EnterTopicDataAccessImpl(NewsFetcherService newsFetcher) {
        this.newsFetcher = newsFetcher;
    }

    @Override
    public List<Article> fetchLeftNews(String topic) {
        return newsFetcher.fetchNews(topic, "left");
    }

    @Override
    public List<Article> fetchRightNews(String topic) {
        return newsFetcher.fetchNews(topic, "right");
    }


}
