package interface_adapter.delete_saved_topic;

public class DeleteSavedTopicViewModel {
    private DeleteSavedTopicState state;

    public DeleteSavedTopicViewModel() {
        state = new DeleteSavedTopicState();
    }

    public DeleteSavedTopicState getState() {
        return state;
    }

    public void setState(DeleteSavedTopicState state) {
        this.state = state;
    }
}
