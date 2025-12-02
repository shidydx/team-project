package use_case.delete_saved_topic;

import entity.Topic;

public interface DeleteSavedTopicDataAccessInterface {
    Topic deleteTopic(String topic_input);
}

