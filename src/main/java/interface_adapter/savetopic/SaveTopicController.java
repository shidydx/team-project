package interface_adapter.savetopic;

import use_case.SaveTopic.SaveTopicInputBoundary;
import use_case.SaveTopic.SaveTopicInputData;

import java.time.LocalDateTime;

public class SaveTopicController {

    private final SaveTopicInputBoundary saveTopicUseCase;

    public SaveTopicController(SaveTopicInputBoundary saveTopicUseCase) {
        this.saveTopicUseCase = saveTopicUseCase;
    }

    public void save(String topic, String username) {
        SaveTopicInputData inputData =
                new SaveTopicInputData(topic, username, LocalDateTime.now());
        saveTopicUseCase.execute(inputData);
    }
}
