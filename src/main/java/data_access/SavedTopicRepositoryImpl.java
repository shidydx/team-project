package data_access;

import java.util.List;
import entity.Topic;
import use_case.delete_saved_topic.DeleteSavedTopicDataAccessInterface;
import use_case.filter_saved_topic.FilterSavedTopicDataAccessInterface;

// in-memory repository for saved topics with duplicate prevention
public class SavedTopicRepositoryImpl implements
        DeleteSavedTopicDataAccessInterface, FilterSavedTopicDataAccessInterface {
    private List<Topic> savedTopics;

    public SavedTopicRepositoryImpl(List<Topic> savedTopics) {
        this.savedTopics = savedTopics;
    }
    
    public void addTopic(Topic topic) {
        if (topic != null && topic.getKeyword() != null && !topic.getKeyword().trim().isEmpty()) {
            boolean exists = false;
            String normalizedNewKeyword = topic.getKeyword().trim().toLowerCase();
            for (Topic t : savedTopics) {
                if (t != null && t.getKeyword() != null) {
                    if (normalizedNewKeyword.equals(t.getKeyword().trim().toLowerCase())) {
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                savedTopics.add(topic);
            }
        }
    }
    
    public List<Topic> getAllTopics() {
        return new java.util.ArrayList<>(savedTopics);
    }

    @Override
    public Topic deleteTopic(String topic_input) {
        if (topic_input == null) return null;
        String normalizedInput = topic_input.toLowerCase().trim();
        java.util.Iterator<Topic> iterator = savedTopics.iterator();
        while (iterator.hasNext()) {
            Topic savedTopic = iterator.next();
            if (savedTopic != null && savedTopic.getKeyword() != null) {
                if (normalizedInput.equals(savedTopic.getKeyword().toLowerCase().trim())) {
                    iterator.remove();
                    return savedTopic;
                }
            }
        }
        return null;
    }

    @Override
    public Topic filterTopic(String topic_input) {
        if (topic_input == null) return null;
        String normalizedInput = topic_input.toLowerCase().trim();
        for (Topic savedTopic : savedTopics) {
            if (savedTopic != null && savedTopic.getKeyword() != null) {
                if (normalizedInput.equals(savedTopic.getKeyword().toLowerCase().trim())) {
                    return savedTopic;
                }
            }
        }
        return null;
    }
}
