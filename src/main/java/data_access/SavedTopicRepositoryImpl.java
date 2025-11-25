package data_access;

import java.util.List;
import entity.Topic;
import use_case.delete_saved_topic.DeleteSavedTopicDataAccessInterface;

public class SavedTopicRepositoryImpl implements DeleteSavedTopicDataAccessInterface {
    private List<Topic> savedTopics;

    public SavedTopicRepositoryImpl(List<Topic> savedTopics) {
        this.savedTopics = savedTopics;
    }

    @Override
    public Topic deleteTopic(String topic_input) {
        for (Topic savedTopic : savedTopics) {
            if (topic_input.toLowerCase().equals(savedTopic.getKeyword())) {
                savedTopics.remove(savedTopic);
                return savedTopic;
            }
        }
        return null;
    }
}
