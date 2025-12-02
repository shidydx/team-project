package use_case.enter_topic;

import entity.Article;

import java.util.List;

public class EnterTopicInteractor implements EnterTopicInputBoundary {
    private final EnterTopicOutputBoundary output;
    private final EnterTopicDataAccessInterface dataAccess;

    public EnterTopicInteractor(EnterTopicOutputBoundary output, EnterTopicDataAccessInterface dataAccess) {
        this.output = output;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(EnterTopicInputData inputData) {
        String keyword = inputData.getTopic();
        if (keyword == null || keyword.trim().isEmpty()) {
            output.prepareFailView("Topic is empty");
        } else {
            
            
            EnterTopicOutputData outputData = new EnterTopicOutputData(inputData.getTopic());
            output.prepareSuccessView(outputData);
        }
    }
}