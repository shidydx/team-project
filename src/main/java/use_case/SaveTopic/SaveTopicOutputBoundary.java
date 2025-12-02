package use_case.SaveTopic;

public interface SaveTopicOutputBoundary {

    void prepareSuccessView(SaveTopicOutputData outputData);

    void prepareFailView(String errorMessage);
}
