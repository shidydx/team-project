package use_case.filter_saved_topic;

import entity.Topic;

public class FilterSavedTopicInteractor implements FilterSavedTopicInputBoundary {
    private final FilterSavedTopicOutputBoundary outputBoundary;
    private final FilterSavedTopicDataAccessInterface dataAccess;

    public FilterSavedTopicInteractor(FilterSavedTopicOutputBoundary outputBoundary,  FilterSavedTopicDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(FilterSavedTopicInputData inputData) {
        String topic_input = inputData.getTopic();
        if (topic_input.isEmpty()) {
            outputBoundary.failureView("Topic input is empty. Please try again.");
        } else {
            try {
                Topic topicFiltered = dataAccess.filterTopic(topic_input);

                if  (topicFiltered == null) {
                    outputBoundary.failureView("No topic named " + topic_input + " was found.");
                } else {
                    String successMsg = "Topic named " + topic_input + " was found.";
                    FilterSavedTopicOutputData outputData = new FilterSavedTopicOutputData(successMsg, "");
                    outputBoundary.successView(outputData);
                }

            } catch (Exception e) {
                outputBoundary.failureView("Error: " + e.getMessage());
            }
        }
    }
}
