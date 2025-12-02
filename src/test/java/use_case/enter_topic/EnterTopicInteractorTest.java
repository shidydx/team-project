package use_case.enter_topic;

import entity.Article;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class EnterTopicInteractorTest {

    @Test
    public void emptyTopicTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {
                fail("expected fail view");
            }

            public void prepareFailView(String errorMessage) {
                assertEquals("Topic is empty", errorMessage);
            }
        };

        EnterTopicDataAccessInterface dataAccess = new EnterTopicDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNews(String topic) {
                fail("Data access should not be called");
                return null;
            }

            @Override
            public List<Article> fetchRightNews(String topic) {
                fail("Data access should not be called");
                return null;
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void invalidLeftArticlesTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("12345678910");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {
                fail("expected fail view");
            }

            public void prepareFailView(String errorMessage) {
            assertEquals("Topic invalid", errorMessage);}
        };

        EnterTopicDataAccessInterface dataAccess = new EnterTopicDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNews(String topic) {
                return new ArrayList<>();
            }
            @Override
            public List<Article> fetchRightNews(String topic) {
                return List.of(new Article("title", "url", "source", "description", "content", "publishdate"));
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void invalidRightArticlesTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("12345678910");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {
                fail("expected fail view");
            }

            public void prepareFailView(String errorMessage) {
                assertEquals("Topic invalid", errorMessage);}
        };

        EnterTopicDataAccessInterface dataAccess = new EnterTopicDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNews(String topic) {
                return List.of(new Article("title", "url", "source", "description", "content", "publishdate"));
            }
            @Override
            public List<Article> fetchRightNews(String topic) {
                return new ArrayList<>();
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }


    @Test
    public void validTopicTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("Bitcoin");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {
                assertEquals("Bitcoin", output.getOutput());
            }

            public void prepareFailView(String errorMessage) {
                fail("Expected success view");
            }
        };
        EnterTopicDataAccessInterface dataAccess = new EnterTopicDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNews(String topic) {
                return List.of(new Article("title", "url", "source", "description", "content", "publishdate"));
            }

            @Override
            public List<Article> fetchRightNews(String topic) {
                return List.of(new Article("title", "url", "source", "description", "content", "publishdate"));
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void failLeftFetchingTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("topic");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {
                fail("expected fail view");
            }

            public void prepareFailView(String errorMessage) {
                assertEquals("failed to fetch left articles", errorMessage);
            }
        };
        EnterTopicDataAccessInterface dataAccess = new EnterTopicDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNews(String topic) {
                throw new RuntimeException();
            }

            @Override
            public List<Article> fetchRightNews(String topic) {
               fail("fetch right news should not be called");
               return null;
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void failRightFetchingTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("topic");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {
                fail("expected fail view");
            }

            public void prepareFailView(String errorMessage) {
                assertEquals("failed to fetch right articles", errorMessage);
            }
        };
        EnterTopicDataAccessInterface dataAccess = new EnterTopicDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNews(String topic) {
                return List.of(new Article("title", "url", "source", "description", "content", "publishdate"));
            }

            @Override
            public List<Article> fetchRightNews(String topic) {
                throw new RuntimeException();
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }
}