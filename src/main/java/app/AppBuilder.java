package app;

import interface_adapter.loadsearch.LoadSearchHistoryController;
import interface_adapter.loadsearch.LoadSearchHistoryPresenter;
import use_case.savetopic.SaveTopicUseCase;

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
    private use_case.loadsearch.SearchHistoryDataAccessInterface searchHistoryDataAccess;
    private interface_adapter.savetopic.SearchHistoryViewModel searchHistoryViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        String newsApiKey = System.getenv("NEWS_API_KEY");
        String openAiApiKey = System.getenv("OPENAI_API_KEY");

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.dataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);

        // *** your in-memory DAO ***
        this.searchHistoryDataAccess =
                new use_case.loadsearch.InMemorySearchHistoryDataAccessObject();
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();

        // *** create your history VM ***
        searchHistoryViewModel = new interface_adapter.savetopic.SearchHistoryViewModel();

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
        interface_adapter.savetopic.SaveTopicPresenter saveTopicPresenter =
                new interface_adapter.savetopic.SaveTopicPresenter(searchHistoryViewModel);

        SaveTopicUseCase saveTopicInteractor =
                new SaveTopicUseCase(
                        searchHistoryDataAccess, saveTopicPresenter);

        interface_adapter.savetopic.SaveTopicController saveTopicController =
                new interface_adapter.savetopic.SaveTopicController(saveTopicInteractor);

        // *** YOUR LOAD use case wiring ***
        LoadSearchHistoryPresenter loadHistoryPresenter =
                new LoadSearchHistoryPresenter(searchHistoryViewModel);

        use_case.loadsearch.LoadSearchHistoryUseCase loadHistoryInteractor =
                new use_case.loadsearch.LoadSearchHistoryUseCase(
                        searchHistoryDataAccess, loadHistoryPresenter);

        LoadSearchHistoryController loadHistoryController =
                new LoadSearchHistoryController(loadHistoryInteractor);

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
