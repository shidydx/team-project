package use_case.FilterSavedTopic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import entity.Topic;
import use_case.filter_saved_topic.*;

public class FilterSavedTopicInteractorTest {
    @Test
    public void topicInputEmpty() {
        FilterSavedTopicInputData inputData = new FilterSavedTopicInputData("");
        FilterSavedTopicOutputBoundary presenter = new FilterSavedTopicOutputBoundary() {
            @Override
            public void successView(FilterSavedTopicOutputData outputData) {
                fail("Expected failure view.");
            }

            @Override
            public void failureView(String errorMsg) {
                assertEquals(errorMsg, "Topic input is empty. Please try again.");
            }
        };

        FilterSavedTopicDataAccessInterface dataAccess = new FilterSavedTopicDataAccessInterface() {
            @Override
            public Topic filterTopic(String topic_input) {
                fail("Expected failure view. Data access shouldn't be called.");
                return null;
            }
        };

        FilterSavedTopicInteractor interactor = new FilterSavedTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void topicInputNotFound() {
        FilterSavedTopicInputData inputData = new FilterSavedTopicInputData("Topic Does not Exist");
        FilterSavedTopicOutputBoundary presenter = new FilterSavedTopicOutputBoundary() {
            @Override
            public void successView(FilterSavedTopicOutputData outputData) {
                fail("Expected failure view.");
            }

            @Override
            public void failureView(String errorMsg) {
                assertEquals(errorMsg, "No topic named " + inputData.getTopic() + " was found.");
            }
        };

        FilterSavedTopicDataAccessInterface dataAccess = new FilterSavedTopicDataAccessInterface() {
            @Override
            public Topic filterTopic(String topic_input) {
                return null;
            }
        };

        FilterSavedTopicInteractor interactor = new FilterSavedTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    public void topicInputFound() {
        FilterSavedTopicInputData inputData = new FilterSavedTopicInputData("Topic Does Exist");
        FilterSavedTopicOutputBoundary presenter = new FilterSavedTopicOutputBoundary() {
            @Override
            public void successView(FilterSavedTopicOutputData outputData) {
                assertEquals(outputData.getFilterSuccessful(), "Topic named " + inputData.getTopic() + " was found.");
            }

            @Override
            public void failureView(String errorMsg) {
                fail("Expected success view.");
            }
        };

        FilterSavedTopicDataAccessInterface dataAccess = new FilterSavedTopicDataAccessInterface() {
            @Override
            public Topic filterTopic(String topic_input) {
                return new Topic(topic_input);
            }
        };

        FilterSavedTopicInteractor interactor = new FilterSavedTopicInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }
}