package use_case.compare_summaries;

public interface CompareSummariesDataAccessInterface {
    String generateComparisonAnalysis(String leftSummary, String rightSummary, String topic);
}

