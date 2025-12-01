package use_case.loadsearch;

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

        // Always append the new entry. This allows the same topic to be
        // stored multiple times (e.g., searches from left and right).
        userHistory.add(entry);
    }
}
