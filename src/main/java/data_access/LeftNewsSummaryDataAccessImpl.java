package data_access;

import entity.Article;
import use_case.NewsFetcherService;
import use_case.OpenAISummarizerService;
import use_case.left_news_summary.LeftNewsSummaryDataAccessInterface;

import java.util.List;

public class LeftNewsSummaryDataAccessImpl implements LeftNewsSummaryDataAccessInterface {
    private final NewsFetcherService newsFetcher;
    private final OpenAISummarizerService summarizer;

    public LeftNewsSummaryDataAccessImpl(NewsFetcherService newsFetcher, OpenAISummarizerService summarizer) {
        this.newsFetcher = newsFetcher;
        this.summarizer = summarizer;
    }

    @Override
    public List<Article> fetchLeftNewsArticles(String topic) {
        return newsFetcher.fetchNews(topic, "left");
    }

    @Override
    public String summarizeLeftNewsArticles(List<Article> articles) {
        return summarizer.summarizeArticles(articles);
    }
}

