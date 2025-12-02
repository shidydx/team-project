package interface_adapter.savetopic;

import use_case.savetopic.SaveTopicOutputBoundary;
import use_case.savetopic.SaveTopicOutputData;

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
        viewModel.setMessage(""); 
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setMessage(errorMessage);
    }
}
