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
            
            String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8);
            java.util.Map<String, List<Article>> articlesBySource = new java.util.LinkedHashMap<>();
            
            System.out.println("\n=== Fetching " + stance + " news for: " + topic + " ===");
            
            for (String source : sources) {
                String url = BASE_URL + "?q=" + encodedTopic + "&sources=" + source + "&pageSize=5&apiKey=" + apiKey;
                
                Request request = new Request.Builder().url(url).get().build();
                
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        List<Article> articles = parseArticles(responseBody);
                        if (!articles.isEmpty()) {
                            String sourceName = articles.get(0).getSourceName();
                            if (sourceName == null || sourceName.isEmpty()) {
                                sourceName = source;
                            }
                            articlesBySource.put(sourceName, articles);
                            System.out.println("  Found " + articles.size() + " articles from " + sourceName);
                        }
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "";
                        System.err.println("  Failed to fetch from " + source + ": " + response.code() + " - " + errorBody);
                    }
                } catch (IOException e) {
                    System.err.println("  Error fetching from " + source + ": " + e.getMessage());
                }
            }
            
            List<Article> selectedArticles = selectArticlesFromDifferentSources(articlesBySource, 3);
            
            if (countUniqueSources(selectedArticles) < 3 && articlesBySource.size() < 3) {
                String sourcesString = String.join(",", sources);
                String url = BASE_URL + "?q=" + encodedTopic + "&sources=" + sourcesString + "&pageSize=10&apiKey=" + apiKey;
                
                Request request = new Request.Builder().url(url).get().build();
                
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        List<Article> combinedArticles = parseArticles(responseBody);
                        
                        java.util.Map<String, List<Article>> combinedBySource = new java.util.LinkedHashMap<>();
                        for (Article article : combinedArticles) {
                            String sourceName = article.getSourceName() != null && !article.getSourceName().isEmpty() 
                                ? article.getSourceName() 
                                : "Unknown";
                            combinedBySource.putIfAbsent(sourceName, new ArrayList<>());
                            combinedBySource.get(sourceName).add(article);
                        }
                        
                        for (String sourceName : combinedBySource.keySet()) {
                            if (!articlesBySource.containsKey(sourceName)) {
                                articlesBySource.put(sourceName, combinedBySource.get(sourceName));
                            }
                        }
                        
                        selectedArticles = selectArticlesFromDifferentSources(articlesBySource, 3);
                        System.out.println("After combined query: Found articles from " + countUniqueSources(selectedArticles) + " sources");
                    }
                } catch (IOException e) {
                    System.err.println("Error with combined query: " + e.getMessage());
                }
            }
            
            System.out.println("Selected " + selectedArticles.size() + " articles from " + countUniqueSources(selectedArticles) + " sources");
            for (Article article : selectedArticles) {
                System.out.println("  - " + article.getSourceName() + ": " + article.getTitle());
            }
            
            return selectedArticles;

        } catch (Exception e) {
            System.err.println("Error fetching news: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private List<Article> selectArticlesFromDifferentSources(java.util.Map<String, List<Article>> articlesBySource, int minSources) {
        if (articlesBySource.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Article> selected = new ArrayList<>();
        java.util.List<String> sourceNames = new ArrayList<>(articlesBySource.keySet());
        
        int availableSources = sourceNames.size();
        int sourcesToGet = Math.min(minSources, availableSources);
        
        for (int i = 0; i < sourcesToGet; i++) {
            String sourceName = sourceNames.get(i);
            List<Article> sourceArticles = articlesBySource.get(sourceName);
            if (!sourceArticles.isEmpty()) {
                selected.add(sourceArticles.get(0));
            }
        }
        
        if (selected.size() < minSources && availableSources > sourcesToGet) {
            for (int i = sourcesToGet; i < sourceNames.size() && selected.size() < minSources; i++) {
                String sourceName = sourceNames.get(i);
                List<Article> sourceArticles = articlesBySource.get(sourceName);
                if (!sourceArticles.isEmpty()) {
                    selected.add(sourceArticles.get(0));
                }
            }
        }
        
        for (String sourceName : sourceNames) {
            List<Article> sourceArticles = articlesBySource.get(sourceName);
            for (int i = 1; i < sourceArticles.size() && selected.size() < 15; i++) {
                selected.add(sourceArticles.get(i));
            }
        }
        
        return selected;
    }
    
    private int countUniqueSources(List<Article> articles) {
        java.util.Set<String> uniqueSources = new java.util.HashSet<>();
        for (Article article : articles) {
            String sourceName = article.getSourceName() != null && !article.getSourceName().isEmpty() 
                ? article.getSourceName() 
                : "Unknown";
            uniqueSources.add(sourceName);
        }
        return uniqueSources.size();
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
