package data_access;

import entity.Topic;
import entity.TopicRepository;
import java.util.List;

public class InMemoryTopicRepository implements TopicRepository {
    private List<Topic> topics;

    public InMemoryTopicRepository(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    @Override
    public void deleteTopic() {}
    public void filterTopic() {}
}
