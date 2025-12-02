package use_case.enter_topic;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


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
        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter);
        interactor.execute(inputData);
    }

    @Test
    public void emptyStringTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("       ");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {fail("expected fail view");}
            public void prepareFailView(String errorMessage) {assertEquals("Topic is empty", errorMessage);}
        };
        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter);
        interactor.execute(inputData);
    }

    @Test
    public void successTest() {
        EnterTopicInputData inputData = new EnterTopicInputData("climate change");
        EnterTopicOutputBoundary presenter = new EnterTopicOutputBoundary() {
            @Override
            public void prepareSuccessView(EnterTopicOutputData output) {assertEquals("climate change", output.getOutput());}
            public void prepareFailView(String errorMessage) {fail("expected success view");}
        };
        EnterTopicInteractor interactor = new EnterTopicInteractor(presenter);
        interactor.execute(inputData);
    }
}