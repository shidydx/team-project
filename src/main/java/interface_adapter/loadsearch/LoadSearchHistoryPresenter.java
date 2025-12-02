package interface_adapter.loadsearch;

import interface_adapter.savetopic.SearchHistoryViewModel;
import use_case.loadsearch.LoadSearchHistoryOutputBoundary;
import use_case.loadsearch.LoadSearchHistoryOutputData;

import java.util.stream.Collectors;

public class LoadSearchHistoryPresenter implements LoadSearchHistoryOutputBoundary {

    private final SearchHistoryViewModel viewModel;

    public LoadSearchHistoryPresenter(SearchHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadSearchHistoryOutputData outputData) {
        if (outputData.isEmpty()) {
            viewModel.setHistory(java.util.List.of());
            viewModel.setMessage("No previous searches found. Try searching for a topic.");
        } else {
            viewModel.setHistory(
                    outputData.getHistoryItems().stream()
                            .map(i -> new SearchHistoryViewModel.HistoryItemVM(
                                    i.getTopic(), i.getSearchedAt()))
                            .collect(Collectors.toList())
            );
            viewModel.setMessage("");
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setMessage(errorMessage); 
    }
}
