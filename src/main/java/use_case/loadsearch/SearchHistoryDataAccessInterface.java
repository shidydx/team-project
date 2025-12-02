package use_case.loadsearch;

import java.util.List;

public interface SearchHistoryDataAccessInterface {

    List<SearchHistoryEntry> getHistoryForUser(String username);

    void save(SearchHistoryEntry entry);
}
