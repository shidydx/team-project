package use_case.enter_topic;

import entity.Article;
import java.util.*;

public interface EnterTopicDataAccessInterface {

    List<Article> fetchLeftNews(String keyword);

    List<Article> fetchRightNews(String keyword);
}
