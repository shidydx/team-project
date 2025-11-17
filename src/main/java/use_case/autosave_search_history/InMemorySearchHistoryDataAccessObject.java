package use_case.autosave_search_history;

import java.util.*;

/**
 * Simple in-memory DAO using a Map.
 * NOTE: this is a temporary implementation for testing and
 *       will be replaced with a persistent DAO later.
 */
public class InMemorySearchHistoryDataAccessObject implements SearchHistoryDataAccessInterface {

    // key: username, value: list of that user’s history entries
    private final Map<String, List<SearchHistoryEntry>> storage = new HashMap<>();

    @Override
    public List<SearchHistoryEntry> getHistoryForUser(String username) {
        return new ArrayList<>(storage.getOrDefault(username, Collections.emptyList()));
    }

    @Override
    public void save(SearchHistoryEntry entry) {
        List<SearchHistoryEntry> userHistory =
                storage.computeIfAbsent(entry.getUsername(), u -> new ArrayList<>());

        // If an equal entry already exists, replace timestamp.
        int index = userHistory.indexOf(entry);
        if (index != -1) {
            userHistory.get(index).setSearchedAt(entry.getSearchedAt());
        } else {
            userHistory.add(entry);
        }
    }
}
