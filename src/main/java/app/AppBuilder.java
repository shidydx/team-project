package app;

import data_access.RightNewsSummaryDataAccessImpl;
import interface_adapter.right_news_summary.RightNewsController;
import interface_adapter.right_news_summary.RightNewsPresenter;
import interface_adapter.enter_topic.EnterTopicController;
import interface_adapter.enter_topic.EnterTopicPresenter;
import use_case.enter_topic.EnterTopicInteractor;
import use_case.save_topic.SaveTopicUseCase;
import view.RightNewsSummaryView;
import view.EnterTopicView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// builder pattern for constructing the application with all views and use cases
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private use_case.NewsFetcherService newsFetcher;
    private use_case.OpenAISummarizerService summarizer;

    private use_case.left_news_summary.LeftNewsSummaryDataAccessInterface dataAccess;

    private view.LeftNewsSummaryView leftNewsSummaryView;
    private interface_adapter.left_news_summary.LeftNewsSummaryViewModel leftNewsSummaryViewModel;

    private view.RightNewsSummaryView rightNewsSummaryView;
    
    private view.SavedTopicsView savedTopicsView;

    private use_case.right_news_summary.RightNewsSummaryDataAccessInterface rightNewsDataAccess;
    private interface_adapter.right_news_summary.RightNewsViewModel rightNewsViewModel;

    private interface_adapter.enter_topic.EnterTopicViewModel enterTopicViewModel;
    private view.EnterTopicView enterTopicView;

    private use_case.comparison.ComparisonDataAccessInterface comparisonDataAccess;
    private interface_adapter.comparison.ComparisonViewModel comparisonViewModel;
    private view.ComparisonView comparisonView;
    
    private data_access.SavedTopicRepositoryImpl savedTopicRepository;
    private interface_adapter.savetopic.SaveTopicController saveTopicController;
    private interface_adapter.delete_saved_topic.DeleteSavedTopicViewModel deleteSavedTopicViewModel;
    private interface_adapter.filter_saved_topic.FilterSavedTopicViewModel filterSavedTopicViewModel;

    // initialize services, data access objects, and repositories
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        String newsApiKey = loadEnvVariable("NEWS_API_KEY");
        String openAiApiKey = loadEnvVariable("OPENAI_API_KEY");
        
        System.out.println("NEWS_API_KEY loaded: " + (newsApiKey != null && !newsApiKey.isEmpty() ? "Yes" : "No"));
        System.out.println("OPENAI_API_KEY loaded: " + (openAiApiKey != null && !openAiApiKey.isEmpty() ? "Yes" : "No"));

        this.newsFetcher = new interface_adapter.NewsAPIClient(newsApiKey);
        this.summarizer = new interface_adapter.OpenAIClient(openAiApiKey);
        this.dataAccess = new data_access.LeftNewsSummaryDataAccessImpl(newsFetcher, summarizer);

        this.rightNewsDataAccess = new RightNewsSummaryDataAccessImpl(newsFetcher);
        this.comparisonDataAccess = new data_access.ComparisonDataAccessImpl(newsFetcher, summarizer);
        
        this.savedTopicRepository = new data_access.SavedTopicRepositoryImpl(new java.util.ArrayList<>());

    }

    public AppBuilder addEnterTopicView() {
        enterTopicViewModel = new interface_adapter.enter_topic.EnterTopicViewModel();
        enterTopicView = new EnterTopicView(enterTopicViewModel);
        cardPanel.add(enterTopicView, enterTopicView.getViewName());
        return this;
    }

    public AppBuilder addEnterTopicUseCase(){
        final EnterTopicPresenter enterTopicPresenter = new EnterTopicPresenter(enterTopicViewModel);
        final EnterTopicInteractor interactor = new EnterTopicInteractor(enterTopicPresenter);
        final EnterTopicController controller = new EnterTopicController(interactor);

        enterTopicView.setController(controller);
        enterTopicView.setCardChange(cardLayout, cardPanel);
        cardPanel.add(enterTopicView, enterTopicView.getViewName());
        return this;
    }

    public AppBuilder addLeftNewsSummaryView() {
        leftNewsSummaryViewModel = new interface_adapter.left_news_summary.LeftNewsSummaryViewModel();

        leftNewsSummaryView = new view.LeftNewsSummaryView(leftNewsSummaryViewModel);

        cardPanel.add(leftNewsSummaryView, leftNewsSummaryView.getViewName());
        return this;
    }

    // wire up left news summary use case and create save topic controller wrapper
    public AppBuilder addLeftNewsSummaryUseCase() {
        final use_case.left_news_summary.LeftNewsSummaryOutputBoundary presenter =
                new interface_adapter.left_news_summary.LeftNewsSummaryPresenter(leftNewsSummaryViewModel);
        final use_case.left_news_summary.LeftNewsSummaryInputBoundary interactor =
                new use_case.left_news_summary.LeftNewsSummaryInteractor(presenter, dataAccess);
        interface_adapter.left_news_summary.LeftNewsSummaryController controller =
                new interface_adapter.left_news_summary.LeftNewsSummaryController(interactor);
        leftNewsSummaryView.setController(controller);
        leftNewsSummaryView.setCardChange(cardLayout, cardPanel);

        if (enterTopicView != null) {
            enterTopicView.setLeftController(controller, leftNewsSummaryViewModel);
        }

        interface_adapter.savetopic.SaveTopicPresenter saveTopicPresenter =
                new interface_adapter.savetopic.SaveTopicPresenter();

        SaveTopicUseCase saveTopicInteractor =
                new SaveTopicUseCase(saveTopicPresenter);

        this.saveTopicController =
                new interface_adapter.savetopic.SaveTopicController(saveTopicInteractor) {
            @Override
            public void save(String topic, String username) {
                super.save(topic, username);
                if (savedTopicRepository != null && topic != null && !topic.trim().isEmpty()) {
                    entity.Topic newTopic = new entity.Topic(topic.trim());
                    savedTopicRepository.addTopic(newTopic);
                }
            }
            
            @Override
            public void saveWithSummaries(String topic, String username, 
                                         String leftSummary, String rightSummary, String comparisonSummary) {
                super.saveWithSummaries(topic, username, leftSummary, rightSummary, comparisonSummary);
                if (savedTopicRepository != null && topic != null && !topic.trim().isEmpty()) {
                    entity.Topic newTopic = new entity.Topic(topic.trim(), leftSummary, rightSummary, comparisonSummary);
                    savedTopicRepository.addTopic(newTopic);
                }
            }
        };

        return this;
    }

    public AppBuilder addRightNewsSummaryView() {
        rightNewsViewModel = new interface_adapter.right_news_summary.RightNewsViewModel();
        return this;
    }

    public AppBuilder addRightNewsSummaryUseCase() {
        RightNewsPresenter presenter = new RightNewsPresenter(rightNewsViewModel);
        use_case.right_news_summary.RightNewsSummaryInteractor interactor
                = new  use_case.right_news_summary.RightNewsSummaryInteractor(
                summarizer, presenter, rightNewsDataAccess);
        RightNewsController controller = new RightNewsController(interactor);
        rightNewsSummaryView  = new RightNewsSummaryView(controller, rightNewsViewModel);
        rightNewsSummaryView.setCardChange(cardLayout, cardPanel);
        cardPanel.add(rightNewsSummaryView, RightNewsSummaryView.VIEW_NAME);

        if (enterTopicView != null) {
            enterTopicView.setRightController(controller, rightNewsViewModel);
        }
        
        return this;
    }

    public AppBuilder addSavedTopicsView() {
        savedTopicsView = new view.SavedTopicsView();
        savedTopicsView.setCardChange(cardLayout, cardPanel);
        cardPanel.add(savedTopicsView, savedTopicsView.getViewName());
        
        return this;
    }

    // wire up delete and filter use cases for saved topics management
    public AppBuilder addSavedTopicsUseCase() {
        if (deleteSavedTopicViewModel == null) {
            deleteSavedTopicViewModel = new interface_adapter.delete_saved_topic.DeleteSavedTopicViewModel();
        }
        interface_adapter.delete_saved_topic.DeleteSavedTopicPresenter deletePresenter =
                new interface_adapter.delete_saved_topic.DeleteSavedTopicPresenter(deleteSavedTopicViewModel);
        use_case.delete_saved_topic.DeleteSavedTopicInteractor deleteInteractor =
                new use_case.delete_saved_topic.DeleteSavedTopicInteractor(deletePresenter, savedTopicRepository);
        interface_adapter.delete_saved_topic.DeleteSavedTopicController deleteController =
                new interface_adapter.delete_saved_topic.DeleteSavedTopicController(deleteInteractor);
        
        if (filterSavedTopicViewModel == null) {
            filterSavedTopicViewModel = new interface_adapter.filter_saved_topic.FilterSavedTopicViewModel();
        }
        interface_adapter.filter_saved_topic.FilterSavedTopicPresenter filterPresenter =
                new interface_adapter.filter_saved_topic.FilterSavedTopicPresenter(filterSavedTopicViewModel);
        use_case.filter_saved_topic.FilterSavedTopicInteractor filterInteractor =
                new use_case.filter_saved_topic.FilterSavedTopicInteractor(filterPresenter, savedTopicRepository);
        interface_adapter.filter_saved_topic.FilterSavedTopicController filterController =
                new interface_adapter.filter_saved_topic.FilterSavedTopicController(filterInteractor);
        
        if (savedTopicsView != null) {
            savedTopicsView.setDeleteTopicController(deleteController);
            savedTopicsView.setFilterTopicController(filterController);
            savedTopicsView.setDeleteTopicViewModel(deleteSavedTopicViewModel);
            savedTopicsView.setFilterTopicViewModel(filterSavedTopicViewModel);
            savedTopicsView.setSavedTopicRepository(savedTopicRepository);
            if (comparisonView != null) {
                savedTopicsView.setComparisonView(comparisonView);
            }
        }
        
        return this;
    }

    public AppBuilder addComparisonView() {
        comparisonViewModel = new interface_adapter.comparison.ComparisonViewModel();
        comparisonView = new view.ComparisonView(comparisonViewModel);
        comparisonView.setCardChange(cardLayout, cardPanel);
        cardPanel.add(comparisonView, comparisonView.getViewName());
        if (savedTopicsView != null) {
            savedTopicsView.setComparisonView(comparisonView);
        }
        return this;
    }

    public AppBuilder addComparisonUseCase() {
        final interface_adapter.comparison.ComparisonPresenter presenter =
                new interface_adapter.comparison.ComparisonPresenter(comparisonViewModel);
        final use_case.comparison.ComparisonInteractor interactor =
                new use_case.comparison.ComparisonInteractor(presenter, comparisonDataAccess);
        final interface_adapter.ComparisonController controller =
                new interface_adapter.ComparisonController(interactor);
        
        comparisonView.setController(controller);
        comparisonView.setCardChange(cardLayout, cardPanel);
        if (saveTopicController != null) {
            comparisonView.setSaveTopicController(saveTopicController);
        }
        return this;
    }

    // finalize application setup and return configured jframe
    public JFrame build() {
        final JFrame application = new JFrame("MiddleGround AI");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (enterTopicView != null && comparisonView != null) {
            enterTopicView.setComparisonView(comparisonView);
        }

        application.add(cardPanel);
        if (enterTopicView != null){
            cardLayout.show(cardPanel, enterTopicView.getViewName());
        }
        else if (rightNewsSummaryView != null){
            cardLayout.show(cardPanel, RightNewsSummaryView.VIEW_NAME);
        }
        else if (leftNewsSummaryView != null) {
            cardLayout.show(cardPanel, leftNewsSummaryView.getViewName());
        }
        return application;
    }
    
    // load environment variables from .env file or system environment
    private String loadEnvVariable(String key) {
        try {
            java.nio.file.Path envPath = java.nio.file.Paths.get("team-project/.env");
            if (!java.nio.file.Files.exists(envPath)) {
                envPath = java.nio.file.Paths.get(".env");
            }
            
            if (java.nio.file.Files.exists(envPath)) {
                List<String> lines = java.nio.file.Files.readAllLines(envPath);
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith(key + "=")) {
                        String value = line.substring(key.length() + 1).trim();
                        
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        return value;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading .env file: " + e.getMessage());
        }
        
        
        return System.getenv(key);
    }
}
