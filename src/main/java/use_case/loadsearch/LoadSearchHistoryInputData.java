package use_case.loadsearch;

public class LoadSearchHistoryInputData {

    private final String username;

    public LoadSearchHistoryInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
