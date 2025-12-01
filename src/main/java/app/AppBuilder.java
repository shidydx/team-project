package app;

import data_access.EnterTopicDataAccessImpl;
import data_access.RightNewsSummaryDataAccessImpl;
import interface_adapter.loadsearch.LoadSearchHistoryController;
import interface_adapter.loadsearch.LoadSearchHistoryPresenter;
import interface_adapter.right_news_summary.RightNewsSummaryController;
import interface_adapter.right_news_summary.RightNewsSummaryPresenter;
import interface_adapter.entertopic.EnterTopicController;
import interface_adapter.entertopic.EnterTopicPresenter;
import interface_adapter.right_news_summary.RightNewsSummaryViewModel;
import use_case.enter_topic.EnterTopicInteractor;
import use_case.savetopic.SaveTopicUseCase;
import view.RightNewsSummaryView;
import view.EnterTopicView;

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

    private view.RightNewsSummaryView rightNewsSummaryView;
    // *** YOUR NEW FIELDS ***
    private use_case.loadsearch.SearchHistoryDataAccessInterface searchHistoryDataAccess;
    private interface_adapter.savetopic.SearchHistoryViewModel searchHistoryViewModel;
    private view.SearchHistoryView searchHistoryView;
    private LoadSearchHistoryController loadHistoryController;

    private use_case.right_news_summary.RightNewsSummaryDataAccessInterface rightNewsDataAccess;
    private RightNewsSummaryViewModel rightNewsViewModel;

    private use_case.enter_topic.EnterTopicDataAccessInterface enterTopicDataAccess;
    private interface_adapter.entertopic.EnterTopicViewModel enterTopicViewModel;
    private view.EnterTopicView enterTopicView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        String newsApiKey = System.getenv("NEWS_API_KEY");
        String openAiApiKey = System.getenv("OPENAI_API_KEY");

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.dataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);

        this.searchHistoryDataAccess =
                new use_case.loadsearch.InMemorySearchHistoryDataAccessObject();
        this.rightNewsDataAccess = new RightNewsSummaryDataAccessImpl(newsFetcher);
        this.enterTopicDataAccess = new EnterTopicDataAccessImpl(newsFetcher);

    }

    public AppBuilder addEnterTopicView() {
        enterTopicViewModel = new interface_adapter.entertopic.EnterTopicViewModel();
        enterTopicView = new EnterTopicView(enterTopicViewModel);
        cardPanel.add(enterTopicView, enterTopicView.getName());
        return this;
    }

    public AppBuilder addEnterTopicUseCase(){
        final EnterTopicPresenter enterTopicPresenter = new EnterTopicPresenter(enterTopicViewModel);
        final EnterTopicInteractor interactor = new EnterTopicInteractor(enterTopicPresenter, enterTopicDataAccess);
        final EnterTopicController controller = new EnterTopicController(interactor);

        enterTopicView.setController(controller);
        cardPanel.add(enterTopicView, enterTopicView.getName());
        return this;
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();

        searchHistoryViewModel = new interface_adapter.savetopic.SearchHistoryViewModel();

        leftNewsSummaryView = new view.LeftNewsSummaryView(leftNewsSummaryViewModel);

        cardPanel.add(leftNewsSummaryView, leftNewsSummaryView.getViewName());
        return this;
    }

    public AppBuilder addLeftNewsSummaryUseCase() {
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary presenter =
                new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary interactor =
                new use_case.left_news_summary.LeftNewsSummaryInteractor(presenter, dataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController controller =
                new interface_adapter.left_news_summary.LeftNewsSummaryController(interactor);
        leftNewsSummaryView.setController(controller);
        leftNewsSummaryView.setCardChange(cardLayout, cardPanel);

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

        loadHistoryController =
                new LoadSearchHistoryController(loadHistoryInteractor);

        return this;
    }

    public AppBuilder addRightNewsSummaryView() {
        rightNewsViewModel = new RightNewsSummaryViewModel();
        return this;
    }

    public AppBuilder addRightNewsSummaryUseCase() {
        RightNewsSummaryPresenter presenter = new RightNewsSummaryPresenter(rightNewsViewModel);
        use_case.right_news_summary.RightNewsSummaryInteractor interactor
                = new  use_case.right_news_summary.RightNewsSummaryInteractor(
                summarizer, presenter, rightNewsDataAccess);

        RightNewsSummaryController controller = new RightNewsSummaryController(interactor);
        rightNewsSummaryView  = new RightNewsSummaryView(controller, rightNewsViewModel);
        rightNewsSummaryView.setCardChange(cardLayout, cardPanel);

        if (leftNewsSummaryView != null) {
            rightNewsSummaryView.setLeftView(leftNewsSummaryView);
            leftNewsSummaryView.setRightView(rightNewsSummaryView);
        }

        cardPanel.add(rightNewsSummaryView, RightNewsSummaryView.VIEW_NAME);
        return this;
    }

    public AppBuilder addSearchHistoryView() {
        if (searchHistoryViewModel == null) {
            searchHistoryViewModel = new interface_adapter.savetopic.SearchHistoryViewModel();
        }
        
        searchHistoryView = new view.SearchHistoryView(searchHistoryViewModel);
        searchHistoryView.setCardChange(cardLayout, cardPanel);
        cardPanel.add(searchHistoryView, searchHistoryView.getViewName());
        
        return this;
    }

    public AppBuilder addSearchHistoryUseCase() {
        if (loadHistoryController == null) {
            LoadSearchHistoryPresenter loadHistoryPresenter =
                    new LoadSearchHistoryPresenter(searchHistoryViewModel);

            use_case.loadsearch.LoadSearchHistoryUseCase loadHistoryInteractor =
                    new use_case.loadsearch.LoadSearchHistoryUseCase(
                            searchHistoryDataAccess, loadHistoryPresenter);

            loadHistoryController =
                    new LoadSearchHistoryController(loadHistoryInteractor);
        }
        
        if (searchHistoryView != null) {
            searchHistoryView.setLoadHistoryController(loadHistoryController);
        }
        
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("News Analysis Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);
        if (rightNewsSummaryView != null){
            cardLayout.show(cardPanel, RightNewsSummaryView.VIEW_NAME);
        }
        else if (leftNewsSummaryView != null) {
            cardLayout.show(cardPanel, leftNewsSummaryView.getViewName());
        }
        return application;
    }
}
