package interface_adapter.enter_topic;

public class EnterTopicViewModel {
    private final EnterTopicState enterTopicState;

    public EnterTopicViewModel() {
        enterTopicState = new EnterTopicState();
    }

    public EnterTopicState getState() {
        return enterTopicState;
    }
}
