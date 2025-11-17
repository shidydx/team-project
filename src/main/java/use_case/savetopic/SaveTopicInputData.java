package use_case.savetopic;

import java.time.LocalDateTime;

public class SaveTopicInputData {

    private final String topic;
    private final String username;
    private final LocalDateTime searchedAt;

    public SaveTopicInputData(String topic, String username, LocalDateTime searchedAt) {
        this.topic = topic;
        this.username = username;
        this.searchedAt = searchedAt;
    }

    public String getTopic() {
        return topic;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getSearchedAt() {
        return searchedAt;
    }
}
