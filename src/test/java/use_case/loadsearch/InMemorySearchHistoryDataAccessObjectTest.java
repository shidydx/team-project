package use_case.loadsearch;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemorySearchHistoryDataAccessObjectTest {

    @Test
    void saveAndRetrieveSingleEntry() {
        InMemorySearchHistoryDataAccessObject dao = new InMemorySearchHistoryDataAccessObject();
        LocalDateTime now = LocalDateTime.now();
        SearchHistoryEntry entry = new SearchHistoryEntry("topic1", "alice", now);

        dao.save(entry);

        List<SearchHistoryEntry> history = dao.getHistoryForUser("alice");
        assertEquals(1, history.size());
        assertEquals("topic1", history.get(0).getTopic());
        assertEquals("alice", history.get(0).getUsername());
        assertEquals(now, history.get(0).getSearchedAt());
    }

    @Test
    void duplicateEntryUpdatesTimestamp() throws InterruptedException {
        InMemorySearchHistoryDataAccessObject dao = new InMemorySearchHistoryDataAccessObject();
        LocalDateTime t1 = LocalDateTime.now();
        SearchHistoryEntry e1 = new SearchHistoryEntry("topicX", "bob", t1);
        dao.save(e1);

        // Create a later timestamp and save an entry with same topic+username
        LocalDateTime t2 = t1.plusSeconds(5);
        SearchHistoryEntry e2 = new SearchHistoryEntry("topicX", "bob", t2);
        dao.save(e2);

        List<SearchHistoryEntry> history = dao.getHistoryForUser("bob");
        assertEquals(1, history.size(), "Duplicate topic should not create a new entry");
        assertEquals(t2, history.get(0).getSearchedAt(), "Timestamp should be updated to the later value");
    }

    @Test
    void separateUsersHaveSeparateHistory() {
        InMemorySearchHistoryDataAccessObject dao = new InMemorySearchHistoryDataAccessObject();
        LocalDateTime now = LocalDateTime.now();
        dao.save(new SearchHistoryEntry("topicA", "user1", now));
        dao.save(new SearchHistoryEntry("topicB", "user2", now));

        List<SearchHistoryEntry> h1 = dao.getHistoryForUser("user1");
        List<SearchHistoryEntry> h2 = dao.getHistoryForUser("user2");

        assertEquals(1, h1.size());
        assertEquals("topicA", h1.get(0).getTopic());
        assertEquals(1, h2.size());
        assertEquals("topicB", h2.get(0).getTopic());
    }
}
