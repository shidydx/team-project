package interface_adapter.loadsearch;

import use_case.loadsearch.LoadSearchHistoryInputBoundary;
import use_case.loadsearch.LoadSearchHistoryInputData;

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
