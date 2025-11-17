package use_case.savetopic;

public interface SaveTopicOutputBoundary {

    void prepareSuccessView(SaveTopicOutputData outputData);

    void prepareFailView(String errorMessage);
}
