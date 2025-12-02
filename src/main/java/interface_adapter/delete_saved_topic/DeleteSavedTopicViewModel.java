package interface_adapter.delete_saved_topic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DeleteSavedTopicViewModel {
    private DeleteSavedTopicState state;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public DeleteSavedTopicViewModel() {
        state = new DeleteSavedTopicState();
    }

    public DeleteSavedTopicState getState() {
        return state;
    }

    public void setState(DeleteSavedTopicState state) {
        DeleteSavedTopicState oldState = this.state;
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

