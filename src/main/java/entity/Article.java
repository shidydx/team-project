package entity;

public class Article {
    private String title;
    private String url;
    private String sourceName;
    private String description;
    private String content;
    private String publishedDate;

    public Article(String title, String url, String sourceName, String description, String content, String publishedDate) {
        this.title = title;
        this.url = url;
        this.sourceName = sourceName;
        this.description = description;
        this.content = content;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

}
