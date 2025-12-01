package interface_adapter.left_news_summary;

import entity.Article;
import use_case.left_news_summary.LeftNewsSummaryOutputBoundary;
import use_case.left_news_summary.LeftNewsSummaryOutputData;

import java.util.List;

public class LeftNewsSummaryPresenter implements LeftNewsSummaryOutputBoundary{
    private final LeftNewsSummaryViewModel viewModel;

    public LeftNewsSummaryPresenter(LeftNewsSummaryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void successView(LeftNewsSummaryOutputData outputData) {
        LeftNewsSummaryState currState = viewModel.getState();
        currState.setSummary(outputData.getSummary());
        currState.setErrorMessage("");
        
        // Store articles
        List<Article> articles = outputData.getArticles();
        currState.setArticles(articles);
        
        // Set first article details if articles exist (similar to right news pattern)
        if (articles != null && !articles.isEmpty()) {
            Article first = articles.get(0);
            currState.setTitle(first.getTitle() != null ? first.getTitle() : "");
            currState.setName(first.getSourceName() != null ? first.getSourceName() : "");
            currState.setUrl(first.getUrl() != null ? first.getUrl() : "");
        } else {
            currState.setTitle("");
            currState.setName("");
            currState.setUrl("");
        }
        
        viewModel.setState(currState);
    }

    @Override
    public void failureView(String errorMessage) {
        LeftNewsSummaryState currState = viewModel.getState();
        currState.setErrorMessage(errorMessage);
        currState.setSummary("");
        currState.setArticles(null);
        currState.setTitle("");
        currState.setName("");
        currState.setUrl("");
        viewModel.setState(currState);
    }
}

