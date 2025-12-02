package use_case.savetopic;

import java.time.LocalDateTime;

public class SaveTopicInputData {

    private final String topic;
    private final String username;
    private final LocalDateTime searchedAt;
    private final String leftSummary;
    private final String rightSummary;
    private final String comparisonSummary;

    public SaveTopicInputData(String topic, String username, LocalDateTime searchedAt) {
        this.topic = topic;
        this.username = username;
        this.searchedAt = searchedAt;
        this.leftSummary = null;
        this.rightSummary = null;
        this.comparisonSummary = null;
    }
    
    public SaveTopicInputData(String topic, String username, LocalDateTime searchedAt,
                             String leftSummary, String rightSummary, String comparisonSummary) {
        this.topic = topic;
        this.username = username;
        this.searchedAt = searchedAt;
        this.leftSummary = leftSummary;
        this.rightSummary = rightSummary;
        this.comparisonSummary = comparisonSummary;
    }

    public String getTopic() {
        return topic;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getSearchedAt() {
        return searchedAt;
    }
    
    public String getLeftSummary() {
        return leftSummary;
    }
    
    public String getRightSummary() {
        return rightSummary;
    }
    
    public String getComparisonSummary() {
        return comparisonSummary;
    }
}
