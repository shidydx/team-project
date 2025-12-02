package use_case.loadsearch;

import java.time.LocalDateTime;
import java.util.Objects;

public class SearchHistoryEntry {

    private String topic;
    private String username;
    private LocalDateTime searchedAt;

    public SearchHistoryEntry(String topic, String username, LocalDateTime searchedAt) {
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

    public void setSearchedAt(LocalDateTime searchedAt) {
        this.searchedAt = searchedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchHistoryEntry)) return false;
        SearchHistoryEntry that = (SearchHistoryEntry) o;
        
        return Objects.equals(topic, that.topic)
                && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, username);
    }
}
