package use_case.delete_saved_topics;

import entity.Topic;

public interface DeleteSavedTopicsDataAccessInterface {
    Topic deleteTopic(String topic_input);
}