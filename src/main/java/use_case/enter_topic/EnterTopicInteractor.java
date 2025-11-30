package use_case.enter_topic;

import entity.Article;

import java.util.List;

public class EnterTopicInteractor implements EnterTopicInputBoundary {
    private final EnterTopicOutputBoundary output;
    private final EnterTopicDataAccessInterface dataAccess;

    public EnterTopicInteractor(EnterTopicOutputBoundary output, EnterTopicDataAccessInterface dataAccess) {
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
                List<Article> leftArticles = dataAccess.fetchLeftNews(keyword);
                if (leftArticles.isEmpty()) {
                    output.prepareFailView("Topic invalid");
                    return;
                }
                try {
                    List<Article> rightArticles = dataAccess.fetchRightNews(keyword);
                    if (rightArticles.isEmpty()) {
                        output.prepareFailView("Topic invalid");
                        return;
                    }
                } catch (Exception e) {
                    output.prepareFailView("failed to fetch right articles");
                    return;
                }
            } catch (Exception e) {
                output.prepareFailView("failed to fetch left articles");
                return;
            }
            EnterTopicOutputData outputData = new EnterTopicOutputData(inputData.getTopic());
            output.prepareSuccessView(outputData);
        }
    }
}