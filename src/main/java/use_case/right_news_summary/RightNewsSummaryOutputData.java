package use_case.right_news_summary;

import entity.Article;
import java.util.List;

public class RightNewsSummaryOutputData {
    private final String ideology;
    private final String summary;
    private final String url;
    private final String title;
    private final String sourceName;
    private final List<Article> articles;

    public RightNewsSummaryOutputData(String ideology, String summary, String url, String title, String sourceName, List<Article> articles) {
        this.ideology = ideology;
        this.summary = summary;
        this.url = url;
        this.title = title;
        this.sourceName = sourceName;
        this.articles = articles;
    }

    public String getIdeology() { return ideology; }
    public String getSummary() { return summary; }
    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public String getSourceName() { return sourceName; }
    public List<Article> getArticles() { return articles; }
}
