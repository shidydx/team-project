package entity;

import java.util.List;

public class Summary {
    private String topic;
    private String stance;
    private String summarizedText;
    private List<Article> articles;

    public Summary(String topic, String stance, String summarizedText, List<Article> articles) {
        this.topic = topic;
        this.stance = stance;
        this.summarizedText = summarizedText;
        this.articles = articles;
    }

    public String getTopic() {
        return topic;
    }

    public String getStance() {
        return stance;
    }

    public String getSummarizedText() {
        return summarizedText;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setStance(String stance) {
        this.stance = stance;
    }

    public void setSummarizedText(String summarizedText) {
        this.summarizedText = summarizedText;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
