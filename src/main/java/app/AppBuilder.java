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

    // NEW: autosave_search_history pieces (all yours)
    private use_case.autosave_search_history.SearchHistoryDataAccessInterface searchHistoryDataAccess; // NEW
    private interface_adapter.autosave_search_history.SearchHistoryViewModel searchHistoryViewModel;   // NEW

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        String newsApiKey = System.getenv("NEWS_API_KEY");
        String openAiApiKey = System.getenv("OPENAI_API_KEY");

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.dataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);

        // NEW: create your in-memory search history DAO
        this.searchHistoryDataAccess =
                new use_case.autosave_search_history.InMemorySearchHistoryDataAccessObject(); // NEW
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();
        leftNewsSummaryView = new view.LeftNewsSummaryView(leftNewsSummaryViewModel);
        cardPanel.add(leftNewsSummaryView, leftNewsSummaryView.getViewName());

        // NEW: create your own ViewModel (not passed into their view, so you’re not touching it)
        searchHistoryViewModel = new interface_adapter.autosave_search_history.SearchHistoryViewModel(); // NEW

        return this;
    }


    public AppBuilder addLeftNewsSummaryUseCase() {
        // ----- existing teammate use case (unchanged) -----
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary presenter =
                new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary interactor =
                new use_case.left_news_summary.LeftNewsSummaryInteractor(presenter, dataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController controller =
                new interface_adapter.left_news_summary.LeftNewsSummaryController(interactor);
        leftNewsSummaryView.setController(controller);

        // ----- NEW: your autosave_search_history use cases -----

        // SaveTopicUseCase wiring
        interface_adapter.autosave_search_history.SaveTopicPresenter saveTopicPresenter =
                new interface_adapter.autosave_search_history.SaveTopicPresenter(searchHistoryViewModel); // NEW

        use_case.autosave_search_history.SaveTopicUseCase saveTopicInteractor =
                new use_case.autosave_search_history.SaveTopicUseCase(
                        searchHistoryDataAccess, saveTopicPresenter); // NEW

        interface_adapter.autosave_search_history.SaveTopicController saveTopicController =
                new interface_adapter.autosave_search_history.SaveTopicController(saveTopicInteractor); // NEW

        // LoadSearchHistoryUseCase wiring
        interface_adapter.autosave_search_history.LoadSearchHistoryPresenter loadHistoryPresenter =
                new interface_adapter.autosave_search_history.LoadSearchHistoryPresenter(searchHistoryViewModel); // NEW

        use_case.autosave_search_history.LoadSearchHistoryUseCase loadHistoryInteractor =
                new use_case.autosave_search_history.LoadSearchHistoryUseCase(
                        searchHistoryDataAccess, loadHistoryPresenter); // NEW

        interface_adapter.autosave_search_history.LoadSearchHistoryController loadHistoryController =
                new interface_adapter.autosave_search_history.LoadSearchHistoryController(loadHistoryInteractor); // NEW

        // NOTE: we are NOT calling anything on leftNewsSummaryView with these controllers,
        // so we don’t modify their view / viewmodel at all. They are available for
        // unit tests or for later UI integration if your team wants it.

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
