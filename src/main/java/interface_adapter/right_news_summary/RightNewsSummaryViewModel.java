package interface_adapter.right_news_summary;

import entity.Article;
import interface_adapter.left_news_summary.LeftNewsSummaryState;

import java.util.List;

public class RightNewsSummaryViewModel {
    RightNewsSummaryState state;

    public RightNewsSummaryViewModel() {
        state = new RightNewsSummaryState();
    }

    public RightNewsSummaryState getState() {
        return state;
    }

    public void setState(RightNewsSummaryState state) {
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
        RightNewsSummaryState currentState = getState();
        currentState.setSummary(summary);
        setState(currentState);
    }

    public void setErrorMessage(String errorMessage) {
        RightNewsSummaryState currentState = getState();
        currentState.setErrorMessage(errorMessage);
        setState(currentState);
    }

    public void setTopic(String topic) {
        RightNewsSummaryState currentState = getState();
        currentState.setTopic(topic);
        setState(currentState);
    }

    public void setArticles(List<Article> articles) {
        RightNewsSummaryState currentState = getState();
        currentState.setArticles(articles);
        setState(currentState);
    }

    public void setTitle(String title) {
        RightNewsSummaryState currentState = getState();
        currentState.setTitle(title);
        setState(currentState);
    }

    public void setName(String name) {
        RightNewsSummaryState currentState = getState();
        currentState.setName(name);
        setState(currentState);
    }

    public void setUrl(String url) {
        RightNewsSummaryState currentState = getState();
        currentState.setUrl(url);
        setState(currentState);
    }
}