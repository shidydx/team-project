package use_case.EnterTopicTest;

import entity.Article;
import org.junit.jupiter.api.Test;
import use_case.enter_topic.*;


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
            public List<Article> fetchNews(String topic) {
                fail("Data access should not be called");
                return null;
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void invalidTopicTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("1234567890");
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
            public List<Article> fetchNews(String topic) {
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
            public List<Article> fetchNews(String topic) {
                return List.of(new Article("title", "url", "source", "description", "content", "publishdate"));
            }
        };

        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }
}
