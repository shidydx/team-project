package interface_adapter.savetopic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.List;

public class SearchHistoryViewModel {

    public static class HistoryItemVM {
        private final String topic;
        private final LocalDateTime searchedAt;

        public HistoryItemVM(String topic, LocalDateTime searchedAt) {
            this.topic = topic;
            this.searchedAt = searchedAt;
        }

        public String getTopic() { return topic; }
        public LocalDateTime getSearchedAt() { return searchedAt; }
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<HistoryItemVM> history;
    private String message; // “No previous searches found”, errors, etc.

    public List<HistoryItemVM> getHistory() { return history; }
    public String getMessage() { return message; }

    public void setHistory(List<HistoryItemVM> history) {
        this.history = history;
        support.firePropertyChange("history", null, history);
    }

    public void setMessage(String message) {
        this.message = message;
        support.firePropertyChange("message", null, message);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
