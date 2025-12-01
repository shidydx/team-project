package interface_adapter;

import use_case.comparison.ComparisonInputBoundary;
import use_case.comparison.ComparisonInputData;

public class ComparisonController {
    private final ComparisonInputBoundary interactor;

    public ComparisonController(ComparisonInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String topic) {
        ComparisonInputData inputData = new ComparisonInputData(topic);
        interactor.execute(inputData);
    }
}
