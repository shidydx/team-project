package interface_adapter.left_news_summary;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LeftNewsSummaryViewModel {
    public static final String LEFT_NEWS_SUMMARY_STATE_PROPERTY = "leftNewsSummaryState";
    private LeftNewsSummaryState state;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public LeftNewsSummaryViewModel() {
        state = new LeftNewsSummaryState();
    }

    public LeftNewsSummaryState getState() {
        return state;
    }

    public void setState(LeftNewsSummaryState state) {
        LeftNewsSummaryState oldState = this.state;
        this.state = state;
        support.firePropertyChange(LEFT_NEWS_SUMMARY_STATE_PROPERTY, oldState, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}

