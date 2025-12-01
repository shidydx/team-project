package interface_adapter.filter_saved_topic;

public class FilterSavedTopicViewModel {
    private FilterSavedTopicState state;

    public FilterSavedTopicViewModel() {
        state = new FilterSavedTopicState();
    }

    public FilterSavedTopicState getState() {
        return state;
    }

    public void setState(FilterSavedTopicState state) {
        this.state = state;
    }
}
