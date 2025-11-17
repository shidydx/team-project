package use_case;

import entity.Article;
import java.util.List;

public interface OpenAISummarizerService {
    String summarizeArticles(List<Article> articles);
}
