package use_case;

import java.util.List;
import entity.NewsRepository;
import entity.SummarizerService;
import entity.Article;
import entity.Summary;
import entity.Comparison;

public class CompareSummariesUseCase {
    private final NewsRepository newsRepo;
    private final SummarizerService summarizer;

    public CompareSummariesUseCase(NewsRepository newsRepo,
                                   SummarizerService summarizer) {
        this.newsRepo = newsRepo;
        this.summarizer = summarizer;
    }

    public Comparison execute(String topic) throws Exception {
        // fetch left and right articles
        List<Article> leftArticles = newsRepo.getArticles(topic, "left");
        if (leftArticles.isEmpty())
            throw new Exception("Unable to fetch left summary.");

        List<Article> rightArticles = newsRepo.getArticles(topic, "right");
        if (rightArticles.isEmpty())
            throw new Exception("Unable to fetch right summary.");

        // summarize both sides
        Summary leftSummary = summarizer.summarize(leftArticles, "left");
        Summary rightSummary = summarizer.summarize(rightArticles, "right");

        // generate comparison paragraph
        String comparisonText =
                summarizer.compare(leftSummary, rightSummary);

        return new Comparison(leftSummary, rightSummary, comparisonText);
    }
}
