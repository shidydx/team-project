package interface_adapter;

import entity.Article;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.NewsFetcherService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NewsAPIClient implements NewsFetcherService {

    private static final String BASE_URL = "https://newsapi.org/v2/everything";
    private static final String[] LEFT_LEANING_SOURCES = {"cnn", "the-guardian-uk", "the-new-york-times", "nbc-news"};
    private static final String[] RIGHT_LEANING_SOURCES = {"fox-news", "breitbart-news", "the-washington-times", "national-review"};
    private final OkHttpClient client;
    private final String apiKey;

    public NewsAPIClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    @Override
    public List<Article> fetchNews(String topic, String stance) {
        try {
            String[] sources;
            if (stance.equalsIgnoreCase("left")) {
                sources = LEFT_LEANING_SOURCES;
            } else {
                sources = RIGHT_LEANING_SOURCES;
            }
            String sourcesString = String.join(",", sources);

            // Build URL with parameters
            String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8);
            String url = BASE_URL + "?q=" + encodedTopic + "&sources=" + sourcesString + "&pageSize=5&apiKey=" + apiKey;

            // Create HTTP request
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            // Execute request
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + ": " + response.body().string());
                }

                // Parse JSON response
                String responseBody = response.body().string();
                return parseArticles(responseBody);
            }

        } catch (IOException e) {
            // Return empty list on error, or you could throw an exception
            System.err.println("Error fetching news: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Article> parseArticles(String jsonResponse) {
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray articlesArray = jsonObject.getJSONArray("articles");

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleJson = articlesArray.getJSONObject(i);

                // Extract article fields
                String title = articleJson.optString("title", "");
                String url = articleJson.optString("url", "");
                String description = articleJson.optString("description", "");
                String content = articleJson.optString("content", "");
                String publishedDate = articleJson.optString("publishedAt", "");

                // Extract source name
                String sourceName = "";
                if (articleJson.has("source") && !articleJson.isNull("source")) {
                    JSONObject source = articleJson.getJSONObject("source");
                    sourceName = source.optString("name", "");
                }

                // Create Article object
                Article article = new Article(title, url, sourceName, description, content, publishedDate);
                articles.add(article);
            }

        } catch (Exception e) {
            System.err.println("Error parsing articles: " + e.getMessage());
        }

        return articles;
    }
}
