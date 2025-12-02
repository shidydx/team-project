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

// client for fetching news articles from newsapi with left/right source filtering
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
            String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8);
            String url = BASE_URL + "?q=" + encodedTopic + "&sources=" + sourcesString + "&pageSize=5&apiKey=" + apiKey;
            
            System.out.println("\n=== Fetching " + stance + " news for: " + topic + " ===");
            System.out.println("URL: " + url.replace(apiKey, "***"));

            Request request = new Request.Builder().url(url).get().build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + ": " + response.body().string());
                }

                String responseBody = response.body().string();
                List<Article> articles = parseArticles(responseBody);
                System.out.println("Found " + articles.size() + " articles");
                for (Article article : articles) {
                    System.out.println("  - " + article.getSourceName() + ": " + article.getTitle());
                }
                return articles;
            }

        } catch (IOException e) {
            System.err.println("Error fetching news: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // parse json response into article entities
    private List<Article> parseArticles(String jsonResponse) {
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray articlesArray = jsonObject.getJSONArray("articles");

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleJson = articlesArray.getJSONObject(i);

                String title = articleJson.optString("title", "");
                String url = articleJson.optString("url", "");
                String description = articleJson.optString("description", "");
                String content = articleJson.optString("content", "");
                String publishedDate = articleJson.optString("publishedAt", "");

                String sourceName = "";
                if (articleJson.has("source") && !articleJson.isNull("source")) {
                    JSONObject source = articleJson.getJSONObject("source");
                    sourceName = source.optString("name", "");
                }
                
                Article article = new Article(title, url, sourceName, description, content, publishedDate);
                articles.add(article);
            }

        } catch (Exception e) {
            System.err.println("Error parsing articles: " + e.getMessage());
        }

        return articles;
    }
}
