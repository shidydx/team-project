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

    // *** YOUR NEW FIELDS ***
    private use_case.autosave_search_history.SearchHistoryDataAccessInterface searchHistoryDataAccess;
    private interface_adapter.autosave_search_history.SearchHistoryViewModel searchHistoryViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        String newsApiKey = System.getenv("NEWS_API_KEY");
        String openAiApiKey = System.getenv("OPENAI_API_KEY");

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.dataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);

        // *** your in-memory DAO ***
        this.searchHistoryDataAccess =
                new use_case.autosave_search_history.InMemorySearchHistoryDataAccessObject();
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();

        // *** create your history VM ***
        searchHistoryViewModel = new interface_adapter.autosave_search_history.SearchHistoryViewModel();

        // *** UPDATED constructor: pass both VMs to the view ***
        leftNewsSummaryView = new view.LeftNewsSummaryView(leftNewsSummaryViewModel, searchHistoryViewModel);

        cardPanel.add(leftNewsSummaryView, leftNewsSummaryView.getViewName());
        return this;
    }

    public AppBuilder addLeftNewsSummaryUseCase() {
        // existing teammate use case
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary presenter =
                new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary interactor =
                new use_case.left_news_summary.LeftNewsSummaryInteractor(presenter, dataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController controller =
                new interface_adapter.left_news_summary.LeftNewsSummaryController(interactor);
        leftNewsSummaryView.setController(controller);

        // *** YOUR SAVE use case wiring ***
        interface_adapter.autosave_search_history.SaveTopicPresenter saveTopicPresenter =
                new interface_adapter.autosave_search_history.SaveTopicPresenter(searchHistoryViewModel);

        use_case.autosave_search_history.SaveTopicUseCase saveTopicInteractor =
                new use_case.autosave_search_history.SaveTopicUseCase(
                        searchHistoryDataAccess, saveTopicPresenter);

        interface_adapter.autosave_search_history.SaveTopicController saveTopicController =
                new interface_adapter.autosave_search_history.SaveTopicController(saveTopicInteractor);

        // *** YOUR LOAD use case wiring ***
        interface_adapter.autosave_search_history.LoadSearchHistoryPresenter loadHistoryPresenter =
                new interface_adapter.autosave_search_history.LoadSearchHistoryPresenter(searchHistoryViewModel);

        use_case.autosave_search_history.LoadSearchHistoryUseCase loadHistoryInteractor =
                new use_case.autosave_search_history.LoadSearchHistoryUseCase(
                        searchHistoryDataAccess, loadHistoryPresenter);

        interface_adapter.autosave_search_history.LoadSearchHistoryController loadHistoryController =
                new interface_adapter.autosave_search_history.LoadSearchHistoryController(loadHistoryInteractor);

        // *** pass your controllers into the view ***
        leftNewsSummaryView.setSaveTopicController(saveTopicController);
        leftNewsSummaryView.setLoadHistoryController(loadHistoryController);

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
