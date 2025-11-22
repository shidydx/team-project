package use_case.loadsearch;

import java.time.LocalDateTime;
import java.util.List;

public class LoadSearchHistoryOutputData {

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
    private final boolean empty;

    public LoadSearchHistoryOutputData(List<HistoryItem> historyItems, boolean empty) {
        this.historyItems = historyItems;
        this.empty = empty;
    }

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }

    public boolean isEmpty() {
        return empty;
    }
}
