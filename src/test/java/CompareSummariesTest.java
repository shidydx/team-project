
import data_access.CompareSummariesDataAccessImpl;
import interface_adapter.ComparisonController;
import interface_adapter.compare_summaries.ComparisonPresenter;
import interface_adapter.compare_summaries.ComparisonViewModel;
import interface_adapter.OpenAIClient;
import use_case.compare_summaries.CompareSummariesInteractor;

import app.EnvLoader;

public class CompareSummariesTest {
    public static void main(String[] args) {
        EnvLoader.loadEnvFile();
        String openAiApiKey = EnvLoader.getEnv("OPENAI_API_KEY");
        if (openAiApiKey == null || openAiApiKey.isEmpty()) {
            System.err.println("Error: OPENAI_API_KEY environment variable not set");
            return;
        }

        ComparisonViewModel viewModel = new ComparisonViewModel();
        ComparisonPresenter presenter = new ComparisonPresenter(viewModel);
        CompareSummariesDataAccessImpl dataAccess = new CompareSummariesDataAccessImpl(new OpenAIClient(openAiApiKey));
        CompareSummariesInteractor interactor = new CompareSummariesInteractor(presenter, dataAccess);
        ComparisonController controller = new ComparisonController(interactor);

        String leftSummary = "The new policy focuses on expanding social programs and increasing funding for public education. Supporters argue this will reduce inequality and provide better opportunities for underserved communities.";
        
        String rightSummary = "The proposed policy represents excessive government spending that will burden taxpayers. Critics suggest focusing on private sector solutions and reducing regulatory barriers to economic growth.";
        
        String topic = "Government Policy";

        System.out.println("Testing comparison feature...");
        System.out.println("Topic: " + topic);
        System.out.println("\nLeft Summary: " + leftSummary);
        System.out.println("\nRight Summary: " + rightSummary);
        System.out.println("\nGenerating comparison analysis...\n");

        controller.execute(leftSummary, rightSummary, topic);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        view.MainView mainView = new view.MainView(viewModel);
        mainView.displayComparison();
    }
}

