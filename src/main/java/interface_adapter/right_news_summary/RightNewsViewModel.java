package interface_adapter.right_news_summary;

import entity.Article;
import java.util.List;

public class RightNewsViewModel {
    private String ideology = "Right";
    private String summary;
    private String url;
    private String title;
    private String name;
    private String errorMessage;
    private List<Article> articles;


    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setArticles(List<Article> articles) {this.articles = articles;}

    public void setTitle(String title) {this.title = title;}

    public void setName(String name) {this.name = name;}

    public String getIdeology() {
        return ideology;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Article> getArticles() {return articles;}

    public String getName() {return name;}

    public String getTitle() {return title;}

    public void firePropertyChanged() {
    }
}