package use_case.autosave_search_history;

import java.util.List;

public interface SearchHistoryDataAccessInterface {

    /**
     * @return all history entries for the given user.
     *         DAO does NOT need to guarantee order – interactor can sort.
     */
    List<SearchHistoryEntry> getHistoryForUser(String username);

    /**
     * Saves or updates a single entry.
     */
    void save(SearchHistoryEntry entry);
}
