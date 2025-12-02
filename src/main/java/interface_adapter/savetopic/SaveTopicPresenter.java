package interface_adapter.savetopic;

import use_case.SaveTopic.SaveTopicOutputBoundary;
import use_case.SaveTopic.SaveTopicOutputData;

public class SaveTopicPresenter implements SaveTopicOutputBoundary {

    public SaveTopicPresenter() {
    }

    @Override
    public void prepareSuccessView(SaveTopicOutputData outputData) {
    }

    @Override
    public void prepareFailView(String errorMessage) {
    }
}
