package data_access;

import use_case.OpenAISummarizerService;
import use_case.compare_summaries.CompareSummariesDataAccessInterface;

public class CompareSummariesDataAccessImpl implements CompareSummariesDataAccessInterface {
    private final OpenAISummarizerService summarizer;

    public CompareSummariesDataAccessImpl(OpenAISummarizerService summarizer) {
        this.summarizer = summarizer;
    }

    @Override
    public String generateComparisonAnalysis(String leftSummary, String rightSummary, String topic) {
        String prompt = buildComparisonPrompt(leftSummary, rightSummary, topic);
        return summarizer.generateAnalysis(prompt);
    }

    private String buildComparisonPrompt(String leftSummary, String rightSummary, String topic) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please provide a detailed comparison and analysis of the following two news summaries about '").append(topic).append("'.\n\n");
        prompt.append("Left-leaning perspective summary:\n");
        prompt.append(leftSummary).append("\n\n");
        prompt.append("Right-leaning perspective summary:\n");
        prompt.append(rightSummary).append("\n\n");
        prompt.append("Please analyze and compare these summaries. Highlight:\n");
        prompt.append("1. Key differences in framing and perspective\n");
        prompt.append("2. Different emphasis or focus areas\n");
        prompt.append("3. Potential biases or viewpoints\n");
        prompt.append("4. Common ground or shared points\n");
        prompt.append("Keep the analysis concise but comprehensive (2-3 paragraphs).");
        return prompt.toString();
    }
}

