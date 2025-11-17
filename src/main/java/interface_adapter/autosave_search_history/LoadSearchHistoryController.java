package interface_adapter.autosave_search_history;

import use_case.autosave_search_history.LoadSearchHistoryInputBoundary;
import use_case.autosave_search_history.LoadSearchHistoryInputData;

public class LoadSearchHistoryController {

    private final LoadSearchHistoryInputBoundary loadSearchHistoryUseCase;

    public LoadSearchHistoryController(LoadSearchHistoryInputBoundary loadSearchHistoryUseCase) {
        this.loadSearchHistoryUseCase = loadSearchHistoryUseCase;
    }

    public void load(String username) {
        LoadSearchHistoryInputData inputData =
                new LoadSearchHistoryInputData(username);
        loadSearchHistoryUseCase.execute(inputData);
    }
}
