package use_case.delete_saved_topic;

public class DeleteSavedTopicOutputData {
    private final String deleteSuccessful;
    private final String deleteFailed;

    public DeleteSavedTopicOutputData(String deleteSuccessful, String deleteFailed) {
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



