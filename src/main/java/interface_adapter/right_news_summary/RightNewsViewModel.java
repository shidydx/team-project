package interface_adapter.right_news_summary;

import entity.Article;

import java.util.List;

public class RightNewsViewModel {
    RightNewsState state;

    public RightNewsViewModel() {
        state = new RightNewsState();
    }

    public RightNewsState getState() {
        return state;
    }

    public void setState(RightNewsState state) {
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
        RightNewsState currentState = getState();
        currentState.setSummary(summary);
        setState(currentState);
    }

    public void setErrorMessage(String errorMessage) {
        RightNewsState currentState = getState();
        currentState.setErrorMessage(errorMessage);
        setState(currentState);
    }

    public void setTopic(String topic) {
        RightNewsState currentState = getState();
        currentState.setTopic(topic);
        setState(currentState);
    }

    public void setArticles(List<Article> articles) {
        RightNewsState currentState = getState();
        currentState.setArticles(articles);
        setState(currentState);
    }

    public void setTitle(String title) {
        RightNewsState currentState = getState();
        currentState.setTitle(title);
        setState(currentState);
    }

    public void setName(String name) {
        RightNewsState currentState = getState();
        currentState.setName(name);
        setState(currentState);
    }

    public void setUrl(String url) {
        RightNewsState currentState = getState();
        currentState.setUrl(url);
        setState(currentState);
    }
}