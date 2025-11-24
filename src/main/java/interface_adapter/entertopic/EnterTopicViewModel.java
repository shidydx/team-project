package interface_adapter.entertopic;

public class EnterTopicViewModel {
    private EnterTopicState enterTopicState;

    public EnterTopicViewModel() {
        enterTopicState = new EnterTopicState();
    }

    public EnterTopicState getState() {
        return enterTopicState;
    }
    public void setEnterTopicState(EnterTopicState enterTopicState) {
        this.enterTopicState = enterTopicState;
    }
}
