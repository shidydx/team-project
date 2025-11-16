package view;

import interface_adapter.left_news_summary.LeftNewsSummaryController;
import interface_adapter.left_news_summary.LeftNewsSummaryState;
import interface_adapter.left_news_summary.LeftNewsSummaryViewModel;

import javax.swing.*;
import java.awt.*;

public class LeftNewsSummaryView extends JPanel {
    public static final String VIEW_NAME = "left_news_summary";

    private final LeftNewsSummaryViewModel viewModel;
    private LeftNewsSummaryController controller;

    private final JTextArea summaryArea = new JTextArea(15, 40);
    private final JLabel errorLabel = new JLabel();

    public LeftNewsSummaryView(LeftNewsSummaryViewModel viewModel) {
        this.viewModel = viewModel;
        initializeUI();
    }

    public void setController(LeftNewsSummaryController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Left-Leaning News Summary"));
        summaryArea.setEditable(false);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setLineWrap(true);
        summaryArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);

        add(summaryPanel, BorderLayout.CENTER);
        add(errorPanel, BorderLayout.SOUTH);
    }
    
    public void updateView() {
        LeftNewsSummaryState state = viewModel.getState();
        
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            errorLabel.setText(state.getErrorMessage());
            summaryArea.setText("");
        } else if (state.getSummary() != null && !state.getSummary().isEmpty()) {
            summaryArea.setText(state.getSummary());
            errorLabel.setText("");
        }
    }
}

