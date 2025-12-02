package interface_adapter.right_news_summary;

import entity.Article;

import java.util.List;

public class RightNewsState {
    private String summary = "";
    private String errorMessage = "";
    private String topic = "";
    private List<Article> articles;
    private String title;
    private String name;
    private String url;

    public RightNewsState(RightNewsState copy) {
        summary = copy.summary;
        errorMessage = copy.errorMessage;
        topic = copy.topic;
        articles = copy.articles;
        title = copy.title;
        name = copy.name;
        url = copy.url;
    }

    public RightNewsState() {}

    public String getSummary() {
        return summary;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTopic() {
        return topic;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
