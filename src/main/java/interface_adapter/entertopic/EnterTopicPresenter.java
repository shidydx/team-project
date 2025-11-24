package interface_adapter.entertopic;


import use_case.enter_topic.EnterTopicOutputBoundary;
import use_case.enter_topic.EnterTopicOutputData;

public class EnterTopicPresenter implements EnterTopicOutputBoundary {
    private final EnterTopicViewModel viewModel;

    public EnterTopicPresenter(EnterTopicViewModel viewModel) {
        if (viewModel != null) {this.viewModel = viewModel;
        } else {this.viewModel = new EnterTopicViewModel();
        }

    }

    @Override
    public void prepareSuccessView (EnterTopicOutputData outputData) {
        EnterTopicState state = viewModel.getState();
        state.setTopic(outputData.getOutput());
        state.setErrorMessage(null);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        EnterTopicState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
    }
}
