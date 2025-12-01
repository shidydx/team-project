package use_case.LeftNewsSummary;

import entity.Article;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import use_case.left_news_summary.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LeftNewsSummaryInteractorTest {
    @Test
    void successTest() {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData("something");
        LeftNewsSummaryDataAccessInterface dataAccess = new LeftNewsSummaryDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Title",
                        "http://something.com/",
                        "Source",
                        "describe",
                        "content",
                        "1919-08-10"
                );
                return List.of(a1);
            }

            @Override
            public String summarizeLeftNewsArticles(List<Article> articles) {
                return "not a summary";
            }
        };

        LeftNewsSummaryInputBoundary interactor = getLeftNewsSummaryInputBoundary(dataAccess);
        interactor.execute(inputData);
    }

    @NotNull
    private static LeftNewsSummaryInputBoundary getLeftNewsSummaryInputBoundary(LeftNewsSummaryDataAccessInterface dataAccess) {
        LeftNewsSummaryOutputBoundary presenter = new LeftNewsSummaryOutputBoundary() {
            @Override
            public void successView(LeftNewsSummaryOutputData data) {
                assertEquals("not a summary", data.getSummary());
                assertEquals("", data.getErrorMessage());
            }

            @Override
            public void failureView(String failedMessage) {
                fail("Use case failure is unexpected: " + failedMessage);
            }
        };
        return new LeftNewsSummaryInteractor(presenter, dataAccess);
    }

    @Test
    void emptyTopicFailureTest() {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData("   ");
        LeftNewsSummaryDataAccessInterface dataAccess = new LeftNewsSummaryDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                return List.of();
            }

            @Override
            public String summarizeLeftNewsArticles(List<Article> articles) {
                return "summary";
            }
        };

        LeftNewsSummaryInputBoundary interactor = getNewsSummaryInputBoundary("Topic is empty", dataAccess);
        interactor.execute(inputData);
    }

    @NotNull
    private static LeftNewsSummaryInputBoundary getNewsSummaryInputBoundary(String expected, LeftNewsSummaryDataAccessInterface dataAccess) {
        LeftNewsSummaryOutputBoundary presenter = new LeftNewsSummaryOutputBoundary() {
            @Override
            public void successView(LeftNewsSummaryOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String failedMessage) {
                assertEquals(expected, failedMessage);
            }
        };
        return new LeftNewsSummaryInteractor(presenter, dataAccess);
    }

    @Test
    void emptyArticlesFailureTest() {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData("something");
        LeftNewsSummaryDataAccessInterface dataAccess = new LeftNewsSummaryDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                return List.of();
            }

            @Override
            public String summarizeLeftNewsArticles(List<Article> articles) {
                return "summary";
            }
        };

        LeftNewsSummaryInputBoundary interactor = getNewsSummaryInputBoundary("No articles found for topic: something", dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void emptySummaryFailureTest() {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData("something");
        LeftNewsSummaryDataAccessInterface dataAccess = new LeftNewsSummaryDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Title",
                        "http://something.com/",
                        "Source",
                        "describe",
                        "content",
                        "1919-08-10"
                );
                return List.of(a1);
            }

            @Override
            public String summarizeLeftNewsArticles(List<Article> articles) {
                return "";
            }
        };

        LeftNewsSummaryInputBoundary interactor = getNewsSummaryInputBoundary("Failed to generate summary", dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void nullSummaryFailureTest() {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData("something");
        LeftNewsSummaryDataAccessInterface dataAccess = new LeftNewsSummaryDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Title",
                        "http://something.com/",
                        "Source",
                        "describe",
                        "content",
                        "1919-08-10"
                );
                return List.of(a1);
            }

            @Override
            public String summarizeLeftNewsArticles(List<Article> articles) {
                return null;
            }
        };

        LeftNewsSummaryInputBoundary interactor = getNewsSummaryInputBoundary("Failed to generate summary", dataAccess);
        interactor.execute(inputData);
    }


    @Test
    void exceptionHandlingFailureTest() {
        LeftNewsSummaryInputData inputData = new LeftNewsSummaryInputData("something");
        LeftNewsSummaryDataAccessInterface dataAccess = new LeftNewsSummaryDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                throw new RuntimeException("Data access error");
            }

            @Override
            public String summarizeLeftNewsArticles(List<Article> articles) {
                return "summary";
            }
        };

        LeftNewsSummaryInputBoundary interactor = getNewsSummaryInputBoundary("Error: Data access error", dataAccess);
        interactor.execute(inputData);
    }
}
