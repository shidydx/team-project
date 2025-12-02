package use_case.filter_saved_topic;

import entity.Topic;

public interface FilterSavedTopicDataAccessInterface {
    Topic filterTopic(String topic_input);
}

