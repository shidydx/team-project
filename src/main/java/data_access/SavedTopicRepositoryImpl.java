package data_access;

import java.util.List;
import entity.Topic;
import use_case.delete_saved_topics.DeleteSavedTopicsDataAccessInterface;

public class SavedTopicRepositoryImpl implements DeleteSavedTopicsDataAccessInterface {
    private List<Topic> savedTopics;

    public SavedTopicRepositoryImpl(List<Topic> savedTopics) {
        this.savedTopics = savedTopics;
    }

    @Override
    public Topic deleteTopic(String topic_input) {
        for (Topic savedTopic : savedTopics) {
            if (topic_input.toLowerCase().equals(savedTopic.getKeyword())) {
                Topic topicDeleted = savedTopic;
                savedTopics.remove(savedTopic);
                return topicDeleted;
            }
        }
        return null;
    }
}
