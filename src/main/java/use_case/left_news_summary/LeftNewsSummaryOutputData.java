package use_case.left_news_summary;

import entity.Article;
import java.util.List;

public class LeftNewsSummaryOutputData {
    private final String summary;
    private final String errorMessage;
    private final List<Article> articles;

    public LeftNewsSummaryOutputData(String summary, String errorMessage, List<Article> articles) {
        this.summary = summary;
        this.errorMessage = errorMessage;
        this.articles = articles;
    }

    public String getSummary() {
        return summary;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Article> getArticles() {
        return articles;
    }
}

