package data_access;

import java.util.List;
import entity.Topic;
import use_case.delete_saved_topic.DeleteSavedTopicDataAccessInterface;
import use_case.filter_saved_topic.FilterSavedTopicDataAccessInterface;

public class SavedTopicRepositoryImpl implements
        DeleteSavedTopicDataAccessInterface, FilterSavedTopicDataAccessInterface {
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

    @Override
    public Topic filterTopic(String topic_input) {
        for (Topic savedTopic : savedTopics) {
            if (topic_input.toLowerCase().equals(savedTopic.getKeyword()));
                return savedTopic;
            }
            return null;
        }
    }