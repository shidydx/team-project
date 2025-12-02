package entity;

import java.util.Objects;

public class Topic {
    private String keyword;
    private String leftSummary;
    private String rightSummary;
    private String comparisonSummary;

    public Topic(String keyword) {
        this.keyword = keyword;
    }
    
    public Topic(String keyword, String leftSummary, String rightSummary, String comparisonSummary) {
        this.keyword = keyword;
        this.leftSummary = leftSummary;
        this.rightSummary = rightSummary;
        this.comparisonSummary = comparisonSummary;
    }

    public String getKeyword() { return keyword; }
    
    public String getLeftSummary() { return leftSummary; }
    
    public String getRightSummary() { return rightSummary; }
    
    public String getComparisonSummary() { return comparisonSummary; }
    
    public void setLeftSummary(String leftSummary) { this.leftSummary = leftSummary; }
    
    public void setRightSummary(String rightSummary) { this.rightSummary = rightSummary; }
    
    public void setComparisonSummary(String comparisonSummary) { this.comparisonSummary = comparisonSummary; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(keyword != null ? keyword.toLowerCase().trim() : null, 
                             topic.keyword != null ? topic.keyword.toLowerCase().trim() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword != null ? keyword.toLowerCase().trim() : null);
    }
}