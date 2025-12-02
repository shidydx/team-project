package use_case.enter_topic;

public class EnterTopicInteractor implements EnterTopicInputBoundary {
    private final EnterTopicOutputBoundary output;

    public EnterTopicInteractor(EnterTopicOutputBoundary output) {
        this.output = output;
    }

    @Override
    public void execute(EnterTopicInputData inputData) {
        String keyword = inputData.getTopic();
        if (keyword == null || keyword.trim().isEmpty()) {
            output.prepareFailView("Topic is empty");
        } else {
            EnterTopicOutputData outputData = new EnterTopicOutputData(inputData.getTopic());
            output.prepareSuccessView(outputData);
        }
    }
}