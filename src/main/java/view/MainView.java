package view;

import interface_adapter.compare_summaries.ComparisonState;
import interface_adapter.compare_summaries.ComparisonViewModel;

public class MainView {
    private final ComparisonViewModel comparisonViewModel;

    public MainView(ComparisonViewModel comparisonViewModel) {
        this.comparisonViewModel = comparisonViewModel;
    }

    public void displayComparison() {
        ComparisonState state = comparisonViewModel.getState();
        
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            System.err.println("Error: " + state.getErrorMessage());
            return;
        }

        System.out.println("=".repeat(80));
        System.out.println("COMPARISON ANALYSIS");
        System.out.println("=".repeat(80));
        System.out.println("\nLEFT-LEANING SUMMARY:");
        System.out.println("-".repeat(80));
        System.out.println(state.getLeftSummary());
        System.out.println("\nRIGHT-LEANING SUMMARY:");
        System.out.println("-".repeat(80));
        System.out.println(state.getRightSummary());
        System.out.println("\nCOMPARISON ANALYSIS:");
        System.out.println("-".repeat(80));
        System.out.println(state.getComparisonAnalysis());
        System.out.println("=".repeat(80));
    }
}
