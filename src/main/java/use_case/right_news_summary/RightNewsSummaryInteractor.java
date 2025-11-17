package use_case.right_news_summary;

import entity.Article;
import use_case.OpenAISummarizerService;

import java.util.List;

public class RightNewsSummaryInteractor implements RightNewsSummaryInputBoundary {
    private final OpenAISummarizerService summarizer;
    private final RightNewsSummaryOutputBoundary presenter;
    private final RightNewsSummaryDataAccessInterface dataAccess;

    public RightNewsSummaryInteractor(OpenAISummarizerService summarizer, RightNewsSummaryOutputBoundary presenter, RightNewsSummaryDataAccessInterface dataAccess) {
        this.summarizer = summarizer;
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(RightNewsSummaryInputData inputData) {
        String topic = inputData.getTopic();
        if (topic == null || topic.trim().isEmpty()) {
            presenter.summarizeFailView("Topic is empty");
            return;
        }
        List<Article> articles = dataAccess.fetchRightNewsArticles(topic);
        if (articles == null || articles.isEmpty()) {
            presenter.summarizeFailView("No article found");
            return;
        }
        String summary = summarizer.summarizeArticles(articles);
        if (summary == null || summary.trim().isEmpty()) {
            presenter.summarizeFailView("Failed to summarize news");
            return;
        }
        Article first = articles.get(0);
        String url = first.getUrl();
        if (url == null || url.isEmpty()) {url = "URL not found";}
        String title = first.getTitle();
        if (title == null) {title = "";}
        String sourceName = first.getSourceName();
        if (sourceName == null) {sourceName = "";}
        RightNewsSummaryOutputData outputData =
                new RightNewsSummaryOutputData("Right", summary, url, title, sourceName, articles);
        presenter.summarizeSuccessView(outputData);
    }
}