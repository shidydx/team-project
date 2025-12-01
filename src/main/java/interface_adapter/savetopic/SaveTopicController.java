package interface_adapter.savetopic;

import use_case.savetopic.SaveTopicInputBoundary;
import use_case.savetopic.SaveTopicInputData;

import java.time.LocalDateTime;

public class SaveTopicController {

    private final SaveTopicInputBoundary saveTopicUseCase;

    public SaveTopicController(SaveTopicInputBoundary saveTopicUseCase) {
        this.saveTopicUseCase = saveTopicUseCase;
    }

    public void save(String topic, String stance) {
        SaveTopicInputData inputData =
                new SaveTopicInputData(topic, stance, LocalDateTime.now());
        saveTopicUseCase.execute(inputData);
    }

}
