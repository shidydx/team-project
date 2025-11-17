package use_case.autosave_search_history;

public class LoadSearchHistoryInputData {

    private final String username;

    public LoadSearchHistoryInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
