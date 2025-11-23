package use_case.delete_saved_topics;

public class DeleteSavedTopicsOutputData {
    private final String deleteSuccessful;
    private final String deleteFailed;

    public DeleteSavedTopicsOutputData(String deleteSuccessful, String deleteFailed) {
        this.deleteSuccessful = deleteSuccessful;
        this.deleteFailed = deleteFailed;
    }

    public String getDeleteSuccessful() {
        return deleteSuccessful;
    }

    public String getDeleteFailed() {
        return deleteFailed;
    }
}
