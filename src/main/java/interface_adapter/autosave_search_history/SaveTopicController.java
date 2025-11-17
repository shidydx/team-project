package interface_adapter.autosave_search_history;

import use_case.autosave_search_history.SaveTopicInputBoundary;
import use_case.autosave_search_history.SaveTopicInputData;

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
