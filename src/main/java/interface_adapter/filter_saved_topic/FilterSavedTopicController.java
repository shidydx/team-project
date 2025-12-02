package interface_adapter.filter_saved_topic;

import use_case.filter_saved_topic.FilterSavedTopicInputBoundary;
import use_case.filter_saved_topic.FilterSavedTopicInputData;

public class FilterSavedTopicController {
    private final FilterSavedTopicInputBoundary inputBoundary;

    public FilterSavedTopicController(FilterSavedTopicInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(String topic) {
        FilterSavedTopicInputData inputData = new FilterSavedTopicInputData(topic);
        inputBoundary.execute(inputData);
    }
}



