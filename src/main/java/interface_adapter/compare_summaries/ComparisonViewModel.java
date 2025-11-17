package interface_adapter.compare_summaries;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ComparisonViewModel {
    public static final String COMPARISON_STATE_PROPERTY = "comparisonState";
    private ComparisonState state;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ComparisonViewModel() {
        state = new ComparisonState();
    }

    public ComparisonState getState() {
        return state;
    }

    public void setState(ComparisonState state) {
        ComparisonState oldState = this.state;
        this.state = state;
        support.firePropertyChange(COMPARISON_STATE_PROPERTY, oldState, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}

