package app;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    
    private use_case.NewsFetcherService newsFetcher;
    private use_case.OpenAISummarizerService summarizer;

    private use_case.left_news_summary.LeftNewsSummaryDataAccessInterface dataAccess;
    
    private view.LeftNewsSummaryView leftNewsSummaryView;
    private interface_adapter.left_news_summary.LeftNewsSummaryViewModel leftNewsSummaryViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        
        String newsApiKey = System.getenv("NEWS_API_KEY");
        String openAiApiKey = System.getenv("OPENAI_API_KEY");

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.dataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();
        leftNewsSummaryView = new view.LeftNewsSummaryView(leftNewsSummaryViewModel);
        cardPanel.add(leftNewsSummaryView, leftNewsSummaryView.getViewName());
        return this;
    }


    public AppBuilder addLeftNewsSummaryUseCase() {
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary presenter = new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary interactor = new use_case.left_news_summary.LeftNewsSummaryInteractor(presenter, dataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController controller = new interface_adapter.left_news_summary.LeftNewsSummaryController(interactor);
        leftNewsSummaryView.setController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("News Analysis Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        if (leftNewsSummaryView != null) {
            cardLayout.show(cardPanel, leftNewsSummaryView.getViewName());
        }
        return application;
    }
}

