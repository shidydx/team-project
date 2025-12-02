package use_case.right_news_summary;

import java.util.List;

import entity.Article;
import use_case.OpenAISummarizerService;

public class RightNewsSummaryInteractor implements RightNewsSummaryInputBoundary {
    private final OpenAISummarizerService summarizer;
    private final RightNewsSummaryOutputBoundary presenter;
    private final RightNewsSummaryDataAccessInterface dataAccess;

    public RightNewsSummaryInteractor(OpenAISummarizerService summarizer,
                                      RightNewsSummaryOutputBoundary presenter,
                                      RightNewsSummaryDataAccessInterface dataAccess) {
        this.summarizer = summarizer;
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(RightNewsSummaryInputData inputData) {
        final String topic = inputData.getTopic();

        if (isInvalidTopic(topic)) {
            presenter.summarizeFailView("Topic is empty");
        }
        else {
            final List<Article> articles = fetchArticles(topic);
            if (articles != null) {
                final String summary = summarizeArticles(articles);
                if (summary != null) {
                    final RightNewsSummaryOutputData outputData = buildOutputData(summary, articles);
                    presenter.summarizeSuccessView(outputData);
                }
            }
        }
    }

    private boolean isInvalidTopic(final String topic) {
        return topic == null || topic.trim().isEmpty();
    }

    private List<Article> fetchArticles(final String topic) {
        List<Article> result = dataAccess.fetchRightNewsArticles(topic);
        if (result == null || result.isEmpty()) {
            presenter.summarizeFailView("No articles found for topic: " + topic);
            result = null;
        }
        return result;
    }

    private String summarizeArticles(final List<Article> articles) {
        String result = summarizer.summarizeArticles(articles);
        if (result == null || result.trim().isEmpty()) {
            presenter.summarizeFailView("Failed to generate summary");
            result = null;
        }
        return result;
    }

    private RightNewsSummaryOutputData buildOutputData(final String summary,
                                                       final List<Article> articles) {
        final Article first = articles.get(0);
        String url = first.getUrl();
        if (url == null || url.isEmpty()) {
            url = "URL not found";
        }
        String title = first.getTitle();
        if (title == null) {
            title = "";
        }
        String sourceName = first.getSourceName();
        if (sourceName == null) {
            sourceName = "";
        }
        return new RightNewsSummaryOutputData("Right", summary, url, title, sourceName, articles);
    }
}
