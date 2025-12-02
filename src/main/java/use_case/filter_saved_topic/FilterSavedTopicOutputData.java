package use_case.filter_saved_topic;

public class FilterSavedTopicOutputData {
    private final String filterSuccessful;
    private final String filterFailed;

    public FilterSavedTopicOutputData(String filterSuccessful, String filterFailed) {
        this.filterSuccessful = filterSuccessful;
        this.filterFailed = filterFailed;
    }

    public String getFilterSuccessful() {
        return filterSuccessful;
    }

    public String getFilterFailed() {
        return filterFailed;
    }
}
