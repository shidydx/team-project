package interface_adapter.savetopic;

import use_case.savetopic.SaveTopicOutputBoundary;
import use_case.savetopic.SaveTopicOutputData;

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
