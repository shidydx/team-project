package use_case.SaveTopic;

import org.junit.jupiter.api.Test;
import use_case.loadsearch.InMemorySearchHistoryDataAccessObject;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveTopicUseCaseTest {

    private static class CapturingPresenter implements SaveTopicOutputBoundary {
        SaveTopicOutputData lastOutput;
        String lastError;

        @Override
        public void prepareSuccessView(SaveTopicOutputData outputData) {
            this.lastOutput = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
        }
    }

    @Test
    void saveNewTopicProducesHistoryItem() {
        InMemorySearchHistoryDataAccessObject dao = new InMemorySearchHistoryDataAccessObject();
        CapturingPresenter presenter = new CapturingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(dao, presenter);

        LocalDateTime now = LocalDateTime.now();
        SaveTopicInputData input = new SaveTopicInputData("t1", "u1", now);
        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        List<SaveTopicOutputData.HistoryItem> items = presenter.lastOutput.getHistoryItems();
        assertEquals(1, items.size());
        assertEquals("t1", items.get(0).getTopic());
        assertEquals(now, items.get(0).getSearchedAt());
    }

    @Test
    void savingSameTopicUpdatesTimestamp() {
        InMemorySearchHistoryDataAccessObject dao = new InMemorySearchHistoryDataAccessObject();
        CapturingPresenter presenter = new CapturingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(dao, presenter);

        LocalDateTime t1 = LocalDateTime.now();
        interactor.execute(new SaveTopicInputData("dup", "u2", t1));

        LocalDateTime t2 = t1.plusSeconds(10);
        interactor.execute(new SaveTopicInputData("dup", "u2", t2));

        assertNull(presenter.lastError);
        List<SaveTopicOutputData.HistoryItem> items = presenter.lastOutput.getHistoryItems();
        assertEquals(1, items.size(), "Duplicate topic should remain single entry");
        assertEquals(t2, items.get(0).getSearchedAt(), "Timestamp should be updated to latest");
    }

    @Test
    void savingDifferentTopicsAddsMultipleEntriesInReverseChronologicalOrder() {
        InMemorySearchHistoryDataAccessObject dao = new InMemorySearchHistoryDataAccessObject();
        CapturingPresenter presenter = new CapturingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(dao, presenter);

        LocalDateTime t1 = LocalDateTime.now();
        interactor.execute(new SaveTopicInputData("first", "u3", t1));

        LocalDateTime t2 = t1.plusSeconds(5);
        interactor.execute(new SaveTopicInputData("second", "u3", t2));

        List<SaveTopicOutputData.HistoryItem> items = presenter.lastOutput.getHistoryItems();
        assertEquals(2, items.size());
        // most recent should be first
        assertEquals("second", items.get(0).getTopic());
        assertEquals(t2, items.get(0).getSearchedAt());
        assertEquals("first", items.get(1).getTopic());
    }
}
