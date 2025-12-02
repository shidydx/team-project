package use_case.comparison;

import entity.Article;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonInteractorTest {
    
    @Test
    void successTest() {
        ComparisonInputData inputData = new ComparisonInputData("climate change");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Left Article",
                        "http://left.com/",
                        "Left Source",
                        "Left description",
                        "Left content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                Article a1 = new Article("Right Article",
                        "http://right.com/",
                        "Right Source",
                        "Right description",
                        "Right content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                if (articles.get(0).getTitle().contains("Left")) {
                    return "Left summary";
                } else {
                    return "Right summary";
                }
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                return "Comparison analysis between perspectives";
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                assertEquals("climate change", data.getTopic());
                assertEquals("Left summary", data.getLeftSummary());
                assertEquals("Right summary", data.getRightSummary());
                assertEquals("Comparison analysis between perspectives", data.getComparisonAnalysis());
                assertEquals(1, data.getLeftArticles().size());
                assertEquals(1, data.getRightArticles().size());
                assertEquals("Left Article", data.getLeftArticles().get(0).getTitle());
                assertEquals("Right Article", data.getRightArticles().get(0).getTitle());
            }

            @Override
            public void failureView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void emptyTopicFailureTest() {
        ComparisonInputData inputData = new ComparisonInputData("   ");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                fail("Data access should not be called when topic is empty");
                return null;
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                fail("Data access should not be called when topic is empty");
                return null;
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                fail("Summarizer should not be called when topic is empty");
                return null;
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                fail("Comparison should not be called when topic is empty");
                return null;
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String error) {
                assertEquals("Topic is empty", error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void noLeftArticlesFailureTest() {
        ComparisonInputData inputData = new ComparisonInputData("test topic");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                return Arrays.asList();
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                Article a1 = new Article("Right Article",
                        "http://right.com/",
                        "Right Source",
                        "Right description",
                        "Right content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                fail("Summarizer should not be called when no left articles found");
                return null;
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                fail("Comparison should not be called when no left articles found");
                return null;
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String error) {
                assertEquals("No left-leaning articles found for topic: test topic", error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void noRightArticlesFailureTest() {
        ComparisonInputData inputData = new ComparisonInputData("test topic");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Left Article",
                        "http://left.com/",
                        "Left Source",
                        "Left description",
                        "Left content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                return Arrays.asList();
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                fail("Summarizer should not be called when no right articles found");
                return null;
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                fail("Comparison should not be called when no right articles found");
                return null;
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String error) {
                assertEquals("No right-leaning articles found for topic: test topic", error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void failedLeftSummaryTest() {
        ComparisonInputData inputData = new ComparisonInputData("test topic");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Left Article",
                        "http://left.com/",
                        "Left Source",
                        "Left description",
                        "Left content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                Article a1 = new Article("Right Article",
                        "http://right.com/",
                        "Right Source",
                        "Right description",
                        "Right content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                if (articles.get(0).getTitle().contains("Left")) {
                    return "";
                } else {
                    return "Right summary";
                }
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                fail("Comparison should not be called when left summary fails");
                return null;
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String error) {
                assertEquals("Failed to generate left summary", error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void failedRightSummaryTest() {
        ComparisonInputData inputData = new ComparisonInputData("test topic");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Left Article",
                        "http://left.com/",
                        "Left Source",
                        "Left description",
                        "Left content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                Article a1 = new Article("Right Article",
                        "http://right.com/",
                        "Right Source",
                        "Right description",
                        "Right content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                if (articles.get(0).getTitle().contains("Left")) {
                    return "Left summary";
                } else {
                    return "";
                }
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                fail("Comparison should not be called when right summary fails");
                return null;
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String error) {
                assertEquals("Failed to generate right summary", error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }

    @Test
    void failedComparisonAnalysisTest() {
        ComparisonInputData inputData = new ComparisonInputData("test topic");
        
        ComparisonDataAccessInterface dataAccess = new ComparisonDataAccessInterface() {
            @Override
            public List<Article> fetchLeftNewsArticles(String topic) {
                Article a1 = new Article("Left Article",
                        "http://left.com/",
                        "Left Source",
                        "Left description",
                        "Left content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public List<Article> fetchRightNewsArticles(String topic) {
                Article a1 = new Article("Right Article",
                        "http://right.com/",
                        "Right Source",
                        "Right description",
                        "Right content",
                        "2024-01-15"
                );
                return Arrays.asList(a1);
            }

            @Override
            public String summarizeArticles(List<Article> articles) {
                if (articles.get(0).getTitle().contains("Left")) {
                    return "Left summary";
                } else {
                    return "Right summary";
                }
            }

            @Override
            public String generateComparison(String leftSummary, String rightSummary) {
                return "";
            }
        };

        ComparisonOutputBoundary presenter = new ComparisonOutputBoundary() {
            @Override
            public void successView(ComparisonOutputData data) {
                fail("Use case success is unexpected");
            }

            @Override
            public void failureView(String error) {
                assertEquals("Failed to generate comparison analysis", error);
            }
        };

        ComparisonInputBoundary interactor = new ComparisonInteractor(presenter, dataAccess);
        interactor.execute(inputData);
    }
}

