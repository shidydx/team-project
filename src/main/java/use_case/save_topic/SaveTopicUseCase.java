package use_case.save_topic;

public class SaveTopicUseCase implements SaveTopicInputBoundary {

    private final SaveTopicOutputBoundary presenter;

    public SaveTopicUseCase(SaveTopicOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveTopicInputData inputData) {
        String topic = inputData.getTopic();

        if (topic == null || topic.trim().isEmpty()) {
            presenter.prepareFailView("Topic cannot be empty.");
            return;
        }

        try {
            SaveTopicOutputData outputData = new SaveTopicOutputData();
            presenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Unable to save topic: " + e.getMessage());
        }
    }
}
