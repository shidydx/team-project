package interface_adapter.delete_saved_topic;

import use_case.delete_saved_topic.DeleteSavedTopicOutputBoundary;
import use_case.delete_saved_topic.DeleteSavedTopicOutputData;

public class DeleteSavedTopicPresenter implements DeleteSavedTopicOutputBoundary {
    private final DeleteSavedTopicViewModel viewModel;

    public DeleteSavedTopicPresenter (DeleteSavedTopicViewModel viewModel) {
        if (viewModel != null) {
            this.viewModel = viewModel;
        } else {
            this.viewModel = new DeleteSavedTopicViewModel();
        }
    }

    @Override
    public void successView(DeleteSavedTopicOutputData outputData) {
        DeleteSavedTopicState state = new DeleteSavedTopicState(viewModel.getState());
        state.setTopic(outputData.getDeleteSuccessful());
        state.setErrorMsg(null);
        viewModel.setState(state);
    }

    @Override
    public void failureView(String errorMsg) {
        DeleteSavedTopicState state = new DeleteSavedTopicState(viewModel.getState());
        state.setErrorMsg(errorMsg);
        viewModel.setState(state);
    }
}

