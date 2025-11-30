package interface_adapter.left_news_summary;

import entity.Article;
import java.util.List;

public class LeftNewsSummaryViewModel {
    LeftNewsSummaryState state;

    public LeftNewsSummaryViewModel() {
        state = new LeftNewsSummaryState();
    }

    public LeftNewsSummaryState getState() {
        return state;
    }

    public void setState(LeftNewsSummaryState state) {
        this.state = state;
    }

    // Convenience getters and setters for easier access
    public String getSummary() {
        return state.getSummary();
    }

    public String getErrorMessage() {
        return state.getErrorMessage();
    }

    public String getTopic() {
        return state.getTopic();
    }

    public List<Article> getArticles() {
        return state.getArticles();
    }

    public String getTitle() {
        return state.getTitle();
    }

    public String getName() {
        return state.getName();
    }

    public String getUrl() {
        return state.getUrl();
    }

    public void setSummary(String summary) {
        LeftNewsSummaryState currentState = getState();
        currentState.setSummary(summary);
        setState(currentState);
    }

    public void setErrorMessage(String errorMessage) {
        LeftNewsSummaryState currentState = getState();
        currentState.setErrorMessage(errorMessage);
        setState(currentState);
    }

    public void setTopic(String topic) {
        LeftNewsSummaryState currentState = getState();
        currentState.setTopic(topic);
        setState(currentState);
    }

    public void setArticles(List<Article> articles) {
        LeftNewsSummaryState currentState = getState();
        currentState.setArticles(articles);
        setState(currentState);
    }

    public void setTitle(String title) {
        LeftNewsSummaryState currentState = getState();
        currentState.setTitle(title);
        setState(currentState);
    }

    public void setName(String name) {
        LeftNewsSummaryState currentState = getState();
        currentState.setName(name);
        setState(currentState);
    }

    public void setUrl(String url) {
        LeftNewsSummaryState currentState = getState();
        currentState.setUrl(url);
        setState(currentState);
    }
}

