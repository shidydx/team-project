package use_case;

import entity.Article;
import java.util.List;

public interface NewsFetcherService {
    List<Article> fetchNews(String topic, String stance);
}
