package interface_adapter;

import entity.Article;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.OpenAISummarizerService;

import java.io.IOException;
import java.util.List;

// client for openai api: summarizes articles and generates comparison analysis
public class OpenAIClient implements OpenAISummarizerService {

    private static final String BASE_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String MODEL = "gpt-3.5-turbo";
    
    private final OkHttpClient client;
    private final String apiKey;

    public OpenAIClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    @Override
    public String summarizeArticles(List<Article> articles) {
        try {
            String prompt = buildPrompt(articles);
            String requestBody = buildRequestBody(prompt);

            Request request = new Request.Builder().url(BASE_URL).post(RequestBody.create(requestBody, JSON)).addHeader("Authorization", "Bearer " + apiKey).addHeader("Content-Type", "application/json").build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + ": " + response.body().string());
                }

                String responseBody = response.body().string();
                String aiSummary = parseSummary(responseBody);
                
                aiSummary = consolidateSummarySections(aiSummary);
                
                String fullSummary = aiSummary + "\n\n" + formatSources(articles);
                return fullSummary;
            }

        } catch (IOException e) {
            System.err.println("Error summarizing articles: " + e.getMessage());
            return "";
        }
    }
    
    private String formatSources(List<Article> articles) {
        StringBuilder sources = new StringBuilder();
        sources.append("SOURCES:\n");
        
        
        java.util.Map<String, String> sourceToUrl = new java.util.LinkedHashMap<>();
        
        for (Article article : articles) {
            String sourceName = article.getSourceName();
            String url = article.getUrl();
            if (sourceName != null && !sourceName.isEmpty()) {
                
                if (!sourceToUrl.containsKey(sourceName) || 
                    (url != null && !url.isEmpty() && (sourceToUrl.get(sourceName) == null || sourceToUrl.get(sourceName).isEmpty()))) {
                    sourceToUrl.put(sourceName, url != null ? url : "");
                }
            }
        }
        
        if (sourceToUrl.isEmpty()) {
            sources.append("• No sources available\n");
        } else {
            for (java.util.Map.Entry<String, String> entry : sourceToUrl.entrySet()) {
                String sourceName = entry.getKey();
                String url = entry.getValue();
                if (url != null && !url.isEmpty()) {
                    
                    sources.append("• ").append(sourceName).append(": ").append(url).append("\n");
                } else {
                    
                    sources.append("• ").append(sourceName).append("\n");
                }
            }
        }
        
        return sources.toString();
    }

    // construct prompt for article summarization with formatting instructions
    private String buildPrompt(List<Article> articles) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please provide a detailed, well-formatted summary of ALL the following news articles combined into ONE unified summary.\n\n");
        prompt.append("IMPORTANT: You must provide EXACTLY ONE 'OVERVIEW:' section and EXACTLY ONE 'KEY POINTS:' section. Do NOT create separate sections for each article.\n\n");
        prompt.append("Format your response as follows:\n");
        prompt.append("1. Start with 'OVERVIEW:' followed by 2-3 paragraphs covering the main story and key points from ALL articles combined\n");
        prompt.append("2. Then add 'KEY POINTS:' followed by 4-6 bullet points of important details from ALL articles combined\n\n");
        prompt.append("Do NOT include a sources section - that will be added separately.\n");
        prompt.append("Do NOT create multiple OVERVIEW or KEY POINTS sections. Only ONE of each.\n");
        prompt.append("Make the summary comprehensive and informative, synthesizing information from all articles.\n\n");
        prompt.append("Articles:\n\n");

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            prompt.append("Article ").append(i + 1).append(":\n");
            prompt.append("Title: ").append(article.getTitle()).append("\n");
            prompt.append("Source: ").append(article.getSourceName() != null ? article.getSourceName() : "Unknown").append("\n");
            if (article.getDescription() != null && !article.getDescription().isEmpty()) {
                prompt.append("Description: ").append(article.getDescription()).append("\n");
            }
            if (article.getContent() != null && !article.getContent().isEmpty()) {
                String content = article.getContent();
                if (content.length() > 800) {
                    content = content.substring(0, 800) + "...";
                }
                prompt.append("Content: ").append(content).append("\n");
            }
            prompt.append("\n");
        }

        return prompt.toString();
    }

    private String buildRequestBody(String prompt) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messages.put(message);
        
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1000);
        requestBody.put("temperature", 0.7);
        
        return requestBody.toString();
    }

    private String parseSummary(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray choices = jsonObject.getJSONArray("choices");
            
            if (!choices.isEmpty()) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                return message.getString("content");
            }
            
            return "";
        } catch (Exception e) {
            System.err.println("Error parsing summary: " + e.getMessage());
            return "";
        }
    }

    @Override
    public String compareArticles(String leftSummary, String rightSummary) {
        try {
            String prompt = buildComparisonPrompt(leftSummary, rightSummary);
            String requestBody = buildComparisonRequestBody(prompt);

            Request request = new Request.Builder().url(BASE_URL).post(RequestBody.create(requestBody, JSON)).addHeader("Authorization", "Bearer " + apiKey).addHeader("Content-Type", "application/json").build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + ": " + response.body().string());
                }

                String responseBody = response.body().string();
                String comparison = parseSummary(responseBody);
                
                comparison = consolidateComparisonSections(comparison);
                
                return comparison;
            }

        } catch (IOException e) {
            System.err.println("Error generating comparison: " + e.getMessage());
            return "";
        }
    }

    // construct prompt for comparing left and right summaries
    private String buildComparisonPrompt(String leftSummary, String rightSummary) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Provide a detailed comparison of these two news summaries from different political perspectives.\n\n");
        prompt.append("Left-leaning Summary:\n");
        prompt.append(leftSummary);
        prompt.append("\n\nRight-leaning Summary:\n");
        prompt.append(rightSummary);
        prompt.append("\n\nFormat your comparison as follows:\n\n");
        prompt.append("OVERVIEW:\n");
        prompt.append("Write 2-3 paragraphs describing the main similarities and differences in how each side covers this topic.\n\n");
        prompt.append("KEY SIMILARITIES:\n");
        prompt.append("List 3-5 bullet points of what both perspectives agree on or cover similarly.\n\n");
        prompt.append("KEY DIFFERENCES:\n");
        prompt.append("List 4-6 bullet points highlighting:\n");
        prompt.append("- Differences in framing or emphasis\n");
        prompt.append("- Differences in tone or language\n");
        prompt.append("- Different facts or angles highlighted\n");
        prompt.append("- Different implications or conclusions drawn\n\n");
        prompt.append("ANALYSIS:\n");
        prompt.append("Provide 1-2 paragraphs analyzing why these perspectives might differ and what it reveals about media bias.\n\n");
        prompt.append("Do not fact-check or declare which side is correct - only compare how each presents the topic.");
        return prompt.toString();
    }

    private String buildComparisonRequestBody(String prompt) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messages.put(message);
        
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1500);
        requestBody.put("temperature", 0.7);
        
        return requestBody.toString();
    }
    
    private String consolidateSummarySections(String summary) {
        if (summary == null || summary.isEmpty()) {
            return summary;
        }
        
        String sourcesSection = "";
        int sourcesIndex = summary.toLowerCase().indexOf("sources:");
        if (sourcesIndex >= 0) {
            sourcesSection = summary.substring(sourcesIndex).trim();
            summary = summary.substring(0, sourcesIndex).trim();
        }
        
        java.util.regex.Pattern overviewPattern = java.util.regex.Pattern.compile("(?i)\\bOVERVIEW\\s*:?\\s*");
        java.util.regex.Pattern keyPointsPattern = java.util.regex.Pattern.compile("(?i)\\bKEY\\s+POINTS\\s*:?\\s*");
        
        java.util.regex.Matcher overviewMatcher = overviewPattern.matcher(summary);
        java.util.regex.Matcher keyPointsMatcher = keyPointsPattern.matcher(summary);
        
        java.util.List<java.util.Map<String, Integer>> sections = new java.util.ArrayList<>();
        
        while (overviewMatcher.find()) {
            java.util.Map<String, Integer> section = new java.util.HashMap<>();
            section.put("type", 0);
            section.put("start", overviewMatcher.start());
            section.put("end", overviewMatcher.end());
            sections.add(section);
        }
        
        while (keyPointsMatcher.find()) {
            java.util.Map<String, Integer> section = new java.util.HashMap<>();
            section.put("type", 1);
            section.put("start", keyPointsMatcher.start());
            section.put("end", keyPointsMatcher.end());
            sections.add(section);
        }
        
        sections.sort((a, b) -> Integer.compare(a.get("start"), b.get("start")));
        
        if (sections.isEmpty()) {
            return summary + (sourcesSection.isEmpty() ? "" : "\n\n" + sourcesSection);
        }
        
        StringBuilder overviewContent = new StringBuilder();
        StringBuilder keyPointsContent = new StringBuilder();
        
        for (int i = 0; i < sections.size(); i++) {
            java.util.Map<String, Integer> section = sections.get(i);
            int type = section.get("type");
            int contentStart = section.get("end");
            int contentEnd = (i + 1 < sections.size()) ? sections.get(i + 1).get("start") : summary.length();
            
            String content = summary.substring(contentStart, contentEnd).trim();
            content = content.replaceAll("(?i)\\bOVERVIEW\\s*:?\\s*", "").trim();
            content = content.replaceAll("(?i)\\bKEY\\s+POINTS\\s*:?\\s*", "").trim();
            
            if (!content.isEmpty()) {
                if (type == 0) {
                    if (overviewContent.length() > 0) {
                        overviewContent.append(" ");
                    }
                    overviewContent.append(content);
                } else {
                    if (keyPointsContent.length() > 0) {
                        keyPointsContent.append("\n");
                    }
                    keyPointsContent.append(content);
                }
            }
        }
        
        StringBuilder result = new StringBuilder();
        if (overviewContent.length() > 0) {
            result.append("OVERVIEW:\n").append(overviewContent.toString().trim()).append("\n\n");
        }
        if (keyPointsContent.length() > 0) {
            result.append("KEY POINTS:\n").append(keyPointsContent.toString().trim());
        }
        
        if (!sourcesSection.isEmpty()) {
            if (result.length() > 0) {
                result.append("\n\n");
            }
            result.append(sourcesSection);
        }
        
        return result.toString().trim();
    }
    
    private String consolidateComparisonSections(String comparison) {
        if (comparison == null || comparison.isEmpty()) {
            return comparison;
        }
        
        java.util.regex.Pattern overviewPattern = java.util.regex.Pattern.compile("(?i)\\bOVERVIEW:?\\s*");
        java.util.regex.Pattern keySimilaritiesPattern = java.util.regex.Pattern.compile("(?i)\\bKEY\\s+SIMILARITIES:?\\s*");
        java.util.regex.Pattern keyDifferencesPattern = java.util.regex.Pattern.compile("(?i)\\bKEY\\s+DIFFERENCES:?\\s*");
        java.util.regex.Pattern analysisPattern = java.util.regex.Pattern.compile("(?i)\\bANALYSIS:?\\s*");
        
        java.util.regex.Matcher overviewMatcher = overviewPattern.matcher(comparison);
        java.util.regex.Matcher keySimilaritiesMatcher = keySimilaritiesPattern.matcher(comparison);
        java.util.regex.Matcher keyDifferencesMatcher = keyDifferencesPattern.matcher(comparison);
        java.util.regex.Matcher analysisMatcher = analysisPattern.matcher(comparison);
        
        java.util.List<Integer> overviewIndices = new java.util.ArrayList<>();
        java.util.List<Integer> keySimilaritiesIndices = new java.util.ArrayList<>();
        java.util.List<Integer> keyDifferencesIndices = new java.util.ArrayList<>();
        java.util.List<Integer> analysisIndices = new java.util.ArrayList<>();
        
        while (overviewMatcher.find()) {
            overviewIndices.add(overviewMatcher.start());
        }
        while (keySimilaritiesMatcher.find()) {
            keySimilaritiesIndices.add(keySimilaritiesMatcher.start());
        }
        while (keyDifferencesMatcher.find()) {
            keyDifferencesIndices.add(keyDifferencesMatcher.start());
        }
        while (analysisMatcher.find()) {
            analysisIndices.add(analysisMatcher.start());
        }
        
        java.util.List<Integer> allIndices = new java.util.ArrayList<>();
        allIndices.addAll(overviewIndices);
        allIndices.addAll(keySimilaritiesIndices);
        allIndices.addAll(keyDifferencesIndices);
        allIndices.addAll(analysisIndices);
        allIndices.sort(Integer::compareTo);
        
        StringBuilder overviewContent = new StringBuilder();
        StringBuilder keySimilaritiesContent = new StringBuilder();
        StringBuilder keyDifferencesContent = new StringBuilder();
        StringBuilder analysisContent = new StringBuilder();
        
        for (int i = 0; i < allIndices.size(); i++) {
            int start = allIndices.get(i);
            int end = (i + 1 < allIndices.size()) ? allIndices.get(i + 1) : comparison.length();
            
            String section = comparison.substring(start, end);
            String content = "";
            
            if (overviewIndices.contains(start)) {
                content = section.replaceFirst("(?i)^OVERVIEW:?\\s*", "").trim();
                content = content.replaceAll("(?i)\\bOVERVIEW:?\\s*", "").trim();
                if (!content.isEmpty()) {
                    if (overviewContent.length() > 0) {
                        overviewContent.append(" ");
                    }
                    overviewContent.append(content);
                }
            } else if (keySimilaritiesIndices.contains(start)) {
                content = section.replaceFirst("(?i)^KEY\\s+SIMILARITIES:?\\s*", "").trim();
                content = content.replaceAll("(?i)\\bKEY\\s+SIMILARITIES:?\\s*", "").trim();
                if (!content.isEmpty()) {
                    if (keySimilaritiesContent.length() > 0) {
                        keySimilaritiesContent.append("\n");
                    }
                    keySimilaritiesContent.append(content);
                }
            } else if (keyDifferencesIndices.contains(start)) {
                content = section.replaceFirst("(?i)^KEY\\s+DIFFERENCES:?\\s*", "").trim();
                content = content.replaceAll("(?i)\\bKEY\\s+DIFFERENCES:?\\s*", "").trim();
                if (!content.isEmpty()) {
                    if (keyDifferencesContent.length() > 0) {
                        keyDifferencesContent.append("\n");
                    }
                    keyDifferencesContent.append(content);
                }
            } else if (analysisIndices.contains(start)) {
                content = section.replaceFirst("(?i)^ANALYSIS:?\\s*", "").trim();
                content = content.replaceAll("(?i)\\bANALYSIS:?\\s*", "").trim();
                if (!content.isEmpty()) {
                    if (analysisContent.length() > 0) {
                        analysisContent.append("\n");
                    }
                    analysisContent.append(content);
                }
            }
        }
        
        if (allIndices.isEmpty()) {
            return comparison;
        }
        
        StringBuilder result = new StringBuilder();
        if (overviewContent.length() > 0) {
            result.append("OVERVIEW:\n").append(overviewContent.toString().trim()).append("\n\n");
        }
        if (keySimilaritiesContent.length() > 0) {
            result.append("KEY SIMILARITIES:\n").append(keySimilaritiesContent.toString().trim()).append("\n\n");
        }
        if (keyDifferencesContent.length() > 0) {
            result.append("KEY DIFFERENCES:\n").append(keyDifferencesContent.toString().trim()).append("\n\n");
        }
        if (analysisContent.length() > 0) {
            result.append("ANALYSIS:\n").append(analysisContent.toString().trim());
        }
        
        return result.toString().trim();
    }
}
