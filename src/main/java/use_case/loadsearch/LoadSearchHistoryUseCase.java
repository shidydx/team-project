package use_case.loadsearch;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LoadSearchHistoryUseCase implements LoadSearchHistoryInputBoundary {

    private final SearchHistoryDataAccessInterface historyGateway;
    private final LoadSearchHistoryOutputBoundary presenter;

    public LoadSearchHistoryUseCase(SearchHistoryDataAccessInterface historyGateway,
                                    LoadSearchHistoryOutputBoundary presenter) {
        this.historyGateway = historyGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadSearchHistoryInputData inputData) {
        String username = inputData.getUsername();

        try {
            List<SearchHistoryEntry> history = historyGateway.getHistoryForUser(username);

            // reverse chronological order
            history.sort(Comparator.comparing(SearchHistoryEntry::getSearchedAt).reversed());

            boolean empty = history.isEmpty();

            LoadSearchHistoryOutputData outputData = new LoadSearchHistoryOutputData(
                    history.stream()
                            .map(e -> new LoadSearchHistoryOutputData.HistoryItem(
                                    e.getTopic(), e.getSearchedAt()))
                            .collect(Collectors.toList()),
                    empty
            );

            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Unable to load search history. Please try again.");
        }
    }
}
