package use_case.DeleteSavedTopic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import entity.Topic;
import use_case.delete_saved_topic.*;

public class DeleteSavedTopicInteractorTest {

    @Test
    public void topicInputEmpty() {
        DeleteSavedTopicInputData topic_input = new DeleteSavedTopicInputData("");
        DeleteSavedTopicOutputBoundary presenter = new DeleteSavedTopicOutputBoundary() {
            @Override
            public void successView(DeleteSavedTopicOutputData outputData) {
                fail("Expected failure view.");
            }

            @Override
            public void failureView(String errorMsg) {
                assertEquals(errorMsg, "Topic input is empty. Please try again.");
            }
        };

        DeleteSavedTopicDataAccessInterface dataAccess = new DeleteSavedTopicDataAccessInterface() {
            @Override
            public Topic deleteTopic(String topic_input) {
                fail("Expected failure view. Data access shouldn't be called.");
                return null;
            }
        };

        DeleteSavedTopicInteractor interactor = new DeleteSavedTopicInteractor(presenter, dataAccess);
        interactor.execute(topic_input);
    }

    @Test
    public void topicInputNotFound() {
        DeleteSavedTopicInputData topic_input = new DeleteSavedTopicInputData("Topic Does Not Exist");
        DeleteSavedTopicOutputBoundary presenter = new DeleteSavedTopicOutputBoundary() {
            @Override
            public void successView(DeleteSavedTopicOutputData outputData) {
                fail("Expected failure view.");
            }

            @Override
            public void failureView(String errorMessage) {
                assertEquals(errorMessage, "No topic named " + topic_input.getTopic() + " was found.");
            }
        };

        DeleteSavedTopicDataAccessInterface dataAccess = new DeleteSavedTopicDataAccessInterface() {
            @Override
            public Topic deleteTopic(String topic_input) {
                return null;
            }
        };

        DeleteSavedTopicInteractor interactor = new DeleteSavedTopicInteractor(presenter, dataAccess);
        interactor.execute(topic_input);
    }

    @Test
    public void topicInputFound() {
        DeleteSavedTopicInputData topic_input = new DeleteSavedTopicInputData("Topic Does Exist");
        DeleteSavedTopicOutputBoundary presenter = new DeleteSavedTopicOutputBoundary() {
            @Override
            public void successView(DeleteSavedTopicOutputData outputData) {
                assertEquals(outputData.getDeleteSuccessful(), "Topic named " + topic_input.getTopic() + " was deleted.");
            }

            @Override
            public void failureView(String errorMsg) {
                fail("Expected success view.");
            }
        };

        DeleteSavedTopicDataAccessInterface dataAccess = new DeleteSavedTopicDataAccessInterface() {
            @Override
            public Topic deleteTopic(String topic_input) {
                return new Topic(topic_input);
            }
        };

        DeleteSavedTopicInteractor interactor = new DeleteSavedTopicInteractor(presenter, dataAccess);
        interactor.execute(topic_input);
    }
}
