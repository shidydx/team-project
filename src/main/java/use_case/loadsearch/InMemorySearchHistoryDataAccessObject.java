package use_case.loadsearch;

import java.util.*;

public class InMemorySearchHistoryDataAccessObject implements SearchHistoryDataAccessInterface {

    private final Map<String, List<SearchHistoryEntry>> storage = new HashMap<>();

    @Override
    public List<SearchHistoryEntry> getHistoryForUser(String username) {
        return new ArrayList<>(storage.getOrDefault(username, Collections.emptyList()));
    }

    @Override
    public void save(SearchHistoryEntry entry) {
        List<SearchHistoryEntry> userHistory =
                storage.computeIfAbsent(entry.getUsername(), u -> new ArrayList<>());

        int index = userHistory.indexOf(entry);
        if (index != -1) {
            userHistory.get(index).setSearchedAt(entry.getSearchedAt());
        } else {
            userHistory.add(entry);
        }
    }
}
