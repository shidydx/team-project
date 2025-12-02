package use_case.filter_saved_topic;

public interface FilterSavedTopicOutputBoundary {
    void successView(FilterSavedTopicOutputData outputData);
    void failureView(String errorMsg);
}



