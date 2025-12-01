package use_case.SaveTopic;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Immutable DTO for the updated history list.
 */
public class SaveTopicOutputData {

    public static class HistoryItem {
        private final String topic;
        private final LocalDateTime searchedAt;

        public HistoryItem(String topic, LocalDateTime searchedAt) {
            this.topic = topic;
            this.searchedAt = searchedAt;
        }

        public String getTopic() {
            return topic;
        }

        public LocalDateTime getSearchedAt() {
            return searchedAt;
        }
    }

    private final List<HistoryItem> historyItems;

    public SaveTopicOutputData(List<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }
}
