package interface_adapter.enter_topic;

import use_case.enter_topic.EnterTopicInputBoundary;
import use_case.enter_topic.EnterTopicInputData;

public class EnterTopicController {
    private final EnterTopicInputBoundary enterTopicInputBoundary;

    public EnterTopicController(EnterTopicInputBoundary enterTopicInputBoundary) {
        this.enterTopicInputBoundary = enterTopicInputBoundary;
    }

    public void execute(String topic) {
        final EnterTopicInputData inputData = new EnterTopicInputData(topic);
        enterTopicInputBoundary.execute(inputData);
    }
}
