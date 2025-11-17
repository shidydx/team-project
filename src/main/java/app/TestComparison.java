package app;

import interface_adapter.ComparisonController;
import interface_adapter.compare_summaries.ComparisonPresenter;
import interface_adapter.compare_summaries.ComparisonViewModel;
import data_access.CompareSummariesDataAccessImpl;
import interface_adapter.OpenAIClient;
import use_case.compare_summaries.CompareSummariesInteractor;
import view.ComparisonView;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestComparison {
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

        ComparisonView comparisonView = new ComparisonView(viewModel);
        comparisonView.setController(controller);

        JFrame frame = new JFrame("Test Comparison Feature");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Test Input"));

        JTextField topicField = new JTextField("Government Policy");
        JTextArea leftSummaryArea = new JTextArea(3, 40);
        leftSummaryArea.setText("The new policy focuses on expanding social programs and increasing funding for public education. Supporters argue this will reduce inequality and provide better opportunities for underserved communities.");
        leftSummaryArea.setLineWrap(true);
        leftSummaryArea.setWrapStyleWord(true);

        JTextArea rightSummaryArea = new JTextArea(3, 40);
        rightSummaryArea.setText("The proposed policy represents excessive government spending that will burden taxpayers. Critics suggest focusing on private sector solutions and reducing regulatory barriers to economic growth.");
        rightSummaryArea.setLineWrap(true);
        rightSummaryArea.setWrapStyleWord(true);

        JButton compareButton = new JButton("Compare Summaries");

        inputPanel.add(new JLabel("Topic:"));
        inputPanel.add(topicField);
        inputPanel.add(new JLabel("Left Summary:"));
        inputPanel.add(new JScrollPane(leftSummaryArea));
        inputPanel.add(new JLabel("Right Summary:"));
        inputPanel.add(new JScrollPane(rightSummaryArea));
        inputPanel.add(compareButton);

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topic = topicField.getText();
                String leftSummary = leftSummaryArea.getText();
                String rightSummary = rightSummaryArea.getText();

                if (topic.isEmpty() || leftSummary.isEmpty() || rightSummary.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields");
                    return;
                }

                controller.execute(leftSummary, rightSummary, topic);
            }
        });

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(comparisonView, BorderLayout.CENTER);

        JButton consoleButton = new JButton("Display in Console");
        consoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainView mainView = new MainView(viewModel);
                mainView.displayComparison();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(consoleButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

