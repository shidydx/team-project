package interface_adapter.comparison;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ComparisonViewModel {
    private final PropertyChangeSupport support;
    private ComparisonState state;

    public ComparisonViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.state = new ComparisonState();
    }

    public ComparisonState getState() {
        return state;
    }

    public void setState(ComparisonState state) {
        ComparisonState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }
}


