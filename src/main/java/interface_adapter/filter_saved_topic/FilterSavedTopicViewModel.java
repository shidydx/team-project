package interface_adapter.filter_saved_topic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class FilterSavedTopicViewModel {
    private FilterSavedTopicState state;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public FilterSavedTopicViewModel() {
        state = new FilterSavedTopicState();
    }

    public FilterSavedTopicState getState() {
        return state;
    }

    public void setState(FilterSavedTopicState state) {
        FilterSavedTopicState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}

