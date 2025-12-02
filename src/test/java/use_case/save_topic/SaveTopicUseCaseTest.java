package use_case.save_topic;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

    private static class ThrowingPresenter implements SaveTopicOutputBoundary {
        String lastError;

        @Override
        public void prepareSuccessView(SaveTopicOutputData outputData) {
            throw new RuntimeException("boom");
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
        }
    }

    @Test
    void inputDataGettersWithoutSummaries() {
        LocalDateTime now = LocalDateTime.of(2020,1,2,3,4);
        SaveTopicInputData d = new SaveTopicInputData("topic", "user", now);
        assertEquals("topic", d.getTopic());
        assertEquals("user", d.getUsername());
        assertEquals(now, d.getSearchedAt());
        assertNull(d.getLeftSummary());
        assertNull(d.getRightSummary());
        assertNull(d.getComparisonSummary());
    }

    @Test
    void inputDataGettersWithSummaries() {
        LocalDateTime now = LocalDateTime.of(2021,2,3,4,5);
        SaveTopicInputData d = new SaveTopicInputData("t", "u", now, "L", "R", "C");
        assertEquals("t", d.getTopic());
        assertEquals("u", d.getUsername());
        assertEquals(now, d.getSearchedAt());
        assertEquals("L", d.getLeftSummary());
        assertEquals("R", d.getRightSummary());
        assertEquals("C", d.getComparisonSummary());
    }

    @Test
    void successWhenTopicIsValid() {
        CapturingPresenter presenter = new CapturingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(presenter);

        interactor.execute(new SaveTopicInputData("valid", "u1", LocalDateTime.now()));

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
    }

    @Test
    void failWhenTopicIsNull() {
        CapturingPresenter presenter = new CapturingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(presenter);

        interactor.execute(new SaveTopicInputData(null, "u", LocalDateTime.now()));

        assertNull(presenter.lastOutput);
        assertEquals("Topic cannot be empty.", presenter.lastError);
    }

    @Test
    void failWhenTopicIsEmptyOrWhitespace() {
        CapturingPresenter presenter = new CapturingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(presenter);

        interactor.execute(new SaveTopicInputData("", "u", LocalDateTime.now()));
        assertEquals("Topic cannot be empty.", presenter.lastError);

        // reset
        presenter = new CapturingPresenter();
        interactor = new SaveTopicUseCase(presenter);
        interactor.execute(new SaveTopicInputData("   ", "u", LocalDateTime.now()));
        assertEquals("Topic cannot be empty.", presenter.lastError);
    }

    @Test
    void prepareSuccessThrowingCausesFailView() {
        ThrowingPresenter presenter = new ThrowingPresenter();
        SaveTopicUseCase interactor = new SaveTopicUseCase(presenter);

    interactor.execute(new SaveTopicInputData("ok", "u", LocalDateTime.now()));
        // The ThrowingPresenter catches the failure message in its prepareFailView implementation
        // which sets lastError when called. Since prepareSuccessView throws, the use case should
        // call prepareFailView with a message containing the thrown exception message.
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.contains("Unable to save topic:"));
        assertTrue(presenter.lastError.contains("boom"));
    }
}
