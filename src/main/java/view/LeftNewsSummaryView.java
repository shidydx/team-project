package view;

import interface_adapter.left_news_summary.LeftNewsSummaryController;
import interface_adapter.left_news_summary.LeftNewsSummaryState;
import interface_adapter.left_news_summary.LeftNewsSummaryViewModel;
import interface_adapter.savetopic.SearchHistoryViewModel;
import interface_adapter.savetopic.SaveTopicController;
import interface_adapter.loadsearch.LoadSearchHistoryController;


import javax.swing.*;
import java.awt.*;

public class LeftNewsSummaryView extends JPanel {
    public static final String VIEW_NAME = "left_news_summary";

    private final LeftNewsSummaryViewModel viewModel;

    // NEW: your autosave stuff
    private final SearchHistoryViewModel searchHistoryViewModel;
    private SaveTopicController saveTopicController;
    private LoadSearchHistoryController loadHistoryController;

    private LeftNewsSummaryController controller;

    private final JTextArea summaryArea = new JTextArea(15, 40);
    private final JLabel errorLabel = new JLabel();

    // NEW: UI for showing history
    private final JTextArea historyArea = new JTextArea(10, 25);
    private final JButton historyButton = new JButton("Show Search History");


    public LeftNewsSummaryView(LeftNewsSummaryViewModel viewModel,
                               SearchHistoryViewModel searchHistoryViewModel) {
        this.viewModel = viewModel;
        this.searchHistoryViewModel = searchHistoryViewModel;
        initializeUI();
        setupHistoryBindings();
    }


    public void setController(LeftNewsSummaryController controller) {
        this.controller = controller;
    }

    public void setSaveTopicController(SaveTopicController controller) {
        this.saveTopicController = controller;
    }

    public void setLoadHistoryController(LoadSearchHistoryController controller) {
        this.loadHistoryController = controller;
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


        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Search History"));

        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyPanel.add(historyScroll, BorderLayout.CENTER);
        historyPanel.add(historyButton, BorderLayout.SOUTH);
        historyButton.addActionListener(e -> {
            if (loadHistoryController != null) {
                // use any username you’re using elsewhere; placeholder if none
                String username = "default-user";
                loadHistoryController.load(username);
            }
        });
        add(summaryPanel, BorderLayout.CENTER);
        add(errorPanel, BorderLayout.SOUTH);
        add(historyPanel, BorderLayout.EAST);
    }

    private void setupHistoryBindings() {
        // listen to changes from the SearchHistoryViewModel
        searchHistoryViewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "history":
                    // rebuild the text in the history area
                    StringBuilder sb = new StringBuilder();
                    for (SearchHistoryViewModel.HistoryItemVM item : searchHistoryViewModel.getHistory()) {
                        sb.append(item.getTopic())
                                .append(" (")
                                .append(item.getSearchedAt())
                                .append(")")
                                .append(System.lineSeparator());
                    }
                    historyArea.setText(sb.toString());
                    break;

                case "message":
                    String msg = searchHistoryViewModel.getMessage();
                    if (msg != null && !msg.isEmpty()) {
                        errorLabel.setText(msg);
                    }
                    break;
            }
        });
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

