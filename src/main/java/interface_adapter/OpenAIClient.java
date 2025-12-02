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

    private String buildPrompt(List<Article> articles) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please provide a detailed, well-formatted summary of the following news articles.\n\n");
        prompt.append("Format your response as follows:\n");
        prompt.append("1. Start with 'OVERVIEW:' followed by 2-3 paragraphs covering the main story and key points\n");
        prompt.append("2. Then add 'KEY POINTS:' followed by 4-6 bullet points of important details\n\n");
        prompt.append("Do NOT include a sources section - that will be added separately.\n");
        prompt.append("Make the summary comprehensive and informative.\n\n");
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
                return parseSummary(responseBody);
            }

        } catch (IOException e) {
            System.err.println("Error generating comparison: " + e.getMessage());
            return "";
        }
    }

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
}
