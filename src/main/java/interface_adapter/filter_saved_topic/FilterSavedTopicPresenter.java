package interface_adapter.filter_saved_topic;

import use_case.filter_saved_topic.FilterSavedTopicOutputBoundary;
import use_case.filter_saved_topic.FilterSavedTopicOutputData;

public class FilterSavedTopicPresenter implements FilterSavedTopicOutputBoundary {
    private FilterSavedTopicViewModel viewModel;

    public FilterSavedTopicPresenter(FilterSavedTopicViewModel viewModel) {
        if  (viewModel != null) {
            this.viewModel = viewModel;
        } else {
            this.viewModel = new FilterSavedTopicViewModel();
        }
    }

    @Override
    public void successView(FilterSavedTopicOutputData outputData) {
        FilterSavedTopicState state = new FilterSavedTopicState(viewModel.getState());
        state.setTopic(outputData.getFilterSuccessful());
        state.setErrorMsg(null);
        viewModel.setState(state);
    }

    @Override
    public void failureView(String errorMsg) {
        FilterSavedTopicState state = new FilterSavedTopicState(viewModel.getState());
        state.setErrorMsg(errorMsg);
        viewModel.setState(state);
    }
}

