package interface_adapter.entertopic;


import use_case.enter_topic.EnterTopicOutputBoundary;
import use_case.enter_topic.EnterTopicOutputData;

public class EnterTopicPresenter implements EnterTopicOutputBoundary {
    private final EnterTopicViewModel viewModel;

    public EnterTopicPresenter(EnterTopicViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView (EnterTopicOutputData outputData) {
        viewModel.setTopic(outputData.getOutput());
        viewModel.setErrorMessage(null);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}
