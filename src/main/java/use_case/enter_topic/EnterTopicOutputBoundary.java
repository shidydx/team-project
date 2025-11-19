package use_case.enter_topic;

public interface EnterTopicOutputBoundary {
    void prepareSuccessView(EnterTopicOutputData output);
    void prepareFailView(String errorMessage);
}
