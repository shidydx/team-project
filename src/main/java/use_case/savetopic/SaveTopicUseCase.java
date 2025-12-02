package use_case.savetopic;

import use_case.loadsearch.SearchHistoryDataAccessInterface;
import use_case.loadsearch.SearchHistoryEntry;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SaveTopicUseCase implements SaveTopicInputBoundary {

    private final SearchHistoryDataAccessInterface historyGateway;
    private final SaveTopicOutputBoundary presenter;

    public SaveTopicUseCase(SearchHistoryDataAccessInterface historyGateway,
                            SaveTopicOutputBoundary presenter) {
        this.historyGateway = historyGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveTopicInputData inputData) {
        String username = inputData.getUsername();
        String topic = inputData.getTopic();
        LocalDateTime timestamp = inputData.getSearchedAt();

        try {
            List<SearchHistoryEntry> history = historyGateway.getHistoryForUser(username);

            history.sort(Comparator.comparing(SearchHistoryEntry::getSearchedAt).reversed());

            if (!history.isEmpty()
                    && history.get(0).getTopic().equalsIgnoreCase(topic)) {
                SearchHistoryEntry mostRecent = history.get(0);
                mostRecent.setSearchedAt(timestamp);
                historyGateway.save(mostRecent);
            } else {
                SearchHistoryEntry entry =
                        new SearchHistoryEntry(topic, username, timestamp);
                historyGateway.save(entry);
                history.add(0, entry);
            }

            history.sort(Comparator.comparing(SearchHistoryEntry::getSearchedAt).reversed());

            SaveTopicOutputData outputData = new SaveTopicOutputData(
                    history.stream()
                            .map(e -> new SaveTopicOutputData.HistoryItem(
                                    e.getTopic(), e.getSearchedAt()))
                            .collect(Collectors.toList())
            );

            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Unable to save search history.");
        }
    }
}
