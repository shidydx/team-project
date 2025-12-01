package interface_adapter.savetopic;

import use_case.SaveTopic.SaveTopicOutputBoundary;
import use_case.SaveTopic.SaveTopicOutputData;

import java.util.stream.Collectors;

public class SaveTopicPresenter implements SaveTopicOutputBoundary {

    private final SearchHistoryViewModel viewModel;

    public SaveTopicPresenter(SearchHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SaveTopicOutputData outputData) {
        viewModel.setHistory(
                outputData.getHistoryItems().stream()
                        .map(i -> new SearchHistoryViewModel.HistoryItemVM(
                                i.getTopic(), i.getSearchedAt()))
                        .collect(Collectors.toList())
        );
        viewModel.setMessage(""); // clear any old error
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setMessage(errorMessage);
    }
}
