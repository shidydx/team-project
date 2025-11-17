package app;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    
    private use_case.NewsFetcherService newsFetcher;
    private use_case.OpenAISummarizerService summarizer;

    private use_case.left_news_summary.LeftNewsSummaryDataAccessInterface leftDataAccess;
    // NOTE: Right news summary is Shengrong's responsibility (User Story #3)
    // The comparison feature expects right summary to be provided from that feature
    private use_case.compare_summaries.CompareSummariesDataAccessInterface compareDataAccess;
    
    private view.LeftNewsSummaryView leftNewsSummaryView;
    private interface_adapter.left_news_summary.LeftNewsSummaryViewModel leftNewsSummaryViewModel;
    
    private view.ComparisonView comparisonView;
    private interface_adapter.compare_summaries.ComparisonViewModel comparisonViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        
        EnvLoader.loadEnvFile();
        String newsApiKey = EnvLoader.getEnv("NEWS_API_KEY");
        String openAiApiKey = EnvLoader.getEnv("OPENAI_API_KEY");

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.leftDataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);
        // NOTE: Right news summary data access will be added when Shengrong implements User Story #3
        this.compareDataAccess = new data_access.CompareSummariesDataAccessImpl(summarizer);
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();
        leftNewsSummaryView = new view.LeftNewsSummaryView(leftNewsSummaryViewModel);
        cardPanel.add(leftNewsSummaryView, leftNewsSummaryView.getViewName());
        return this;
    }


    public AppBuilder addLeftNewsSummaryUseCase() {
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary presenter = new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary interactor = new use_case.left_news_summary.LeftNewsSummaryInteractor(presenter, leftDataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController controller = new interface_adapter.left_news_summary.LeftNewsSummaryController(interactor);
        leftNewsSummaryView.setController(controller);
        // Connect comparison view if it exists
        if (comparisonView != null) {
            leftNewsSummaryView.setComparisonView(comparisonView);
        }
        return this;
    }

    public AppBuilder addComparisonView() {
        comparisonViewModel = new interface_adapter.compare_summaries.ComparisonViewModel();
        comparisonView = new view.ComparisonView(comparisonViewModel);
        cardPanel.add(comparisonView, comparisonView.getViewName());
        return this;
    }

    public AppBuilder addComparisonUseCase() {
        final use_case.compare_summaries.CompareSummariesOutputBoundary presenter = new interface_adapter.compare_summaries.ComparisonPresenter(comparisonViewModel);
        final use_case.compare_summaries.CompareSummariesInputBoundary interactor = new use_case.compare_summaries.CompareSummariesInteractor(presenter, compareDataAccess);
        interface_adapter.ComparisonController controller = new interface_adapter.ComparisonController(interactor);
        comparisonView.setController(controller);
        
        // Set up left summary controller for fetching left summaries directly in comparison view
        if (leftNewsSummaryViewModel == null) {
            leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();
        }
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary leftPresenter = new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary leftInteractor = new use_case.left_news_summary.LeftNewsSummaryInteractor(leftPresenter, leftDataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController leftController = new interface_adapter.left_news_summary.LeftNewsSummaryController(leftInteractor);
        comparisonView.setLeftSummaryController(leftController, leftNewsSummaryViewModel);
        
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("News Analysis Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1000, 700); // Set explicit size
        application.setMinimumSize(new Dimension(800, 600));

        application.setLayout(new BorderLayout());
        application.add(cardPanel, BorderLayout.CENTER);

        // Start on comparison view instead of left summary view
        if (comparisonView != null) {
            cardLayout.show(cardPanel, comparisonView.getViewName());
        } else if (leftNewsSummaryView != null) {
            cardLayout.show(cardPanel, leftNewsSummaryView.getViewName());
        }
        return application;
    }
}

