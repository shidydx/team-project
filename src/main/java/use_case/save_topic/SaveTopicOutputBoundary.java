package use_case.save_topic;

public interface SaveTopicOutputBoundary {

    void prepareSuccessView(SaveTopicOutputData outputData);

    void prepareFailView(String errorMessage);
}
