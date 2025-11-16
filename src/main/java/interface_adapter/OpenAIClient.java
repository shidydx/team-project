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

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(RequestBody.create(requestBody, JSON))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + ": " + response.body().string());
                }

                String responseBody = response.body().string();
                return parseSummary(responseBody);
            }

        } catch (IOException e) {
            System.err.println("Error summarizing articles: " + e.getMessage());
            return "";
        }
    }

    private String buildPrompt(List<Article> articles) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please provide a concise summary of the following news articles. ");
        prompt.append("Focus on the main points and key information. ");
        prompt.append("Keep the summary to 2-3 paragraphs.\n\n");

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            prompt.append("Article ").append(i + 1).append(":\n");
            prompt.append("Title: ").append(article.getTitle()).append("\n");
            if (article.getDescription() != null && !article.getDescription().isEmpty()) {
                prompt.append("Description: ").append(article.getDescription()).append("\n");
            }
            if (article.getContent() != null && !article.getContent().isEmpty()) {
                String content = article.getContent();
                if (content.length() > 500) {
                    content = content.substring(0, 500) + "...";
                }
                prompt.append("Content: ").append(content).append("\n");
            }
            prompt.append("\n");
        }

        prompt.append("Please provide a comprehensive summary of these articles.");
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
        requestBody.put("max_tokens", 500);
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
}
