package use_case.RightNewsSummary;

import entity.Article;
import org.junit.jupiter.api.Test;
import use_case.right_news_summary.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RightNewsSummaryInteractorTest {
    @Test
    void successTest() {
        RightNewsSummaryInputData inputData = new RightNewsSummaryInputData("something");
        RightNewsSummaryDataAccessInterface dataAccess = new RightNewsSummaryDataAccessInterface() {
                    @Override
                    public List<Article> fetchRightNewsArticles(String topic) {
                        Article a1 = new Article("Title",
                                "http://something.com/",
                                "Source",
                                "describe",
                                "content",
                                "1919-08-10"
                        );
                        return Arrays.asList(a1);
                    }
                };

        use_case.OpenAISummarizerService summarizer = new use_case.OpenAISummarizerService() {
                    @Override
                    public String summarizeArticles(List<Article> articles) {
                        return "not a summary";
                    }
                };

        RightNewsSummaryOutputBoundary presenter = new RightNewsSummaryOutputBoundary() {
                    @Override
                    public void summarizeSuccessView(RightNewsSummaryOutputData data) {
                        assertEquals("Right", data.getIdeology());
                        assertEquals("not a summary", data.getSummary());
                        assertEquals("http://something.com/", data.getUrl());
                        assertEquals("Title", data.getTitle());
                        assertEquals("Source", data.getSourceName());
                        assertEquals(1, data.getArticles().size());
                    }

                    @Override
                    public void summarizeFailView(String failedMessage) {
                        fail("Use case failure is unexpected: " + failedMessage);
                    }
                };
        RightNewsSummaryInputBoundary interactor = new RightNewsSummaryInteractor(summarizer, presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void emptyTopicFailureTest() {RightNewsSummaryInputData inputData = new RightNewsSummaryInputData("   ");
        RightNewsSummaryDataAccessInterface dataAccess = new RightNewsSummaryDataAccessInterface() {
                    @Override
                    public List<Article> fetchRightNewsArticles(String topic) {
                        fail("Data access should not be called when topic is empty");
                        return null;
                    }
                };
        use_case.OpenAISummarizerService summarizer = new use_case.OpenAISummarizerService() {
                    @Override
                    public String summarizeArticles(List<Article> articles) {
                        fail("Summarizer should not be called when topic is empty");
                        return null;
                    }
                };
        RightNewsSummaryOutputBoundary presenter = new RightNewsSummaryOutputBoundary() {
                    @Override
                    public void summarizeSuccessView(RightNewsSummaryOutputData data) {
                        fail("Use case success is unexpected");
                    }

                    @Override
                    public void summarizeFailView(String failedMessage) {
                        assertEquals("Topic is empty", failedMessage);
                    }
                };
        RightNewsSummaryInputBoundary interactor = new RightNewsSummaryInteractor(summarizer, presenter, dataAccess);
        interactor.execute(inputData);
    }
}
