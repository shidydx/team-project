package data_access;

import entity.Article;
import use_case.NewsFetcherService;
import use_case.OpenAISummarizerService;
import use_case.comparison.ComparisonDataAccessInterface;

import java.util.List;

// data access implementation for comparison: fetches articles and generates summaries/comparisons
public class ComparisonDataAccessImpl implements ComparisonDataAccessInterface {
    private final NewsFetcherService newsFetcher;
    private final OpenAISummarizerService summarizer;

    public ComparisonDataAccessImpl(NewsFetcherService newsFetcher, OpenAISummarizerService summarizer) {
        this.newsFetcher = newsFetcher;
        this.summarizer = summarizer;
    }

    @Override
    public List<Article> fetchLeftNewsArticles(String topic) {
        return newsFetcher.fetchNews(topic, "left");
    }

    @Override
    public List<Article> fetchRightNewsArticles(String topic) {
        return newsFetcher.fetchNews(topic, "right");
    }

    @Override
    public String summarizeArticles(List<Article> articles) {
        return summarizer.summarizeArticles(articles);
    }

    @Override
    public String generateComparison(String leftSummary, String rightSummary) {
        return summarizer.compareArticles(leftSummary, rightSummary);
    }
}


