package use_case.enter_topic;

import entity.Article;

import java.util.List;

public class EnterTopicUseCase  implements EnterTopicInputBoundary{
    private final EnterTopicOutputBoundary output;
    private final EnterTopicDataAccessInterface dataAccess;

    public EnterTopicUseCase(EnterTopicOutputBoundary output, EnterTopicDataAccessInterface dataAccess) {
        this.output = output;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(EnterTopicInputData inputData) {
        String keyword = inputData.getTopic();
        if (keyword.isEmpty()) {
            output.prepareFailView("Topic is empty");
        } else {
            try {
                List<Article> articles = dataAccess.fetchNews(keyword);
                if (articles.isEmpty()) {
                    output.prepareFailView("Topic invalid");
                    return;
                }
            } catch (Exception e) {
                output.prepareFailView(e.getMessage());
            }
            EnterTopicOutputData outputData = new EnterTopicOutputData(inputData.getTopic());
            output.prepareSuccessView(outputData);
        }
    }
}
