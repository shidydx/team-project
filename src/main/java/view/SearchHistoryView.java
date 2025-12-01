package view;

import interface_adapter.loadsearch.LoadSearchHistoryController;
import interface_adapter.savetopic.SearchHistoryViewModel;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SearchHistoryView extends JPanel {
    public static final String VIEW_NAME = "search_history";

    private final SearchHistoryViewModel viewModel;
    private LoadSearchHistoryController loadHistoryController;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JTextArea historyArea;
    private JButton loadHistoryButton;
    private JButton backButton;
    private JLabel messageLabel;

    public SearchHistoryView(SearchHistoryViewModel viewModel) {
        this.viewModel = viewModel;
        initializeUI();
        setupPropertyChangeListener();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Search History"));

        // History display area
        historyArea = new JTextArea(15, 40);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Message label for errors/empty state
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        messagePanel.add(messageLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loadHistoryButton = new JButton("Load Search History");
        backButton = new JButton("Back to Left News");
        
        loadHistoryButton.addActionListener(e -> {
            if (loadHistoryController != null) {
                String username = "default-user"; // TODO: Get actual username if needed
                loadHistoryController.load(username);
            }
        });
        
        backButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, LeftNewsSummaryView.VIEW_NAME);
            }
        });
        
        buttonPanel.add(loadHistoryButton);
        buttonPanel.add(backButton);

        // Add components
        add(mainPanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupPropertyChangeListener() {
        viewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "history":
                    updateHistoryDisplay();
                    break;
                case "message":
                    String msg = viewModel.getMessage();
                    if (msg != null && !msg.isEmpty()) {
                        messageLabel.setText(msg);
                    } else {
                        messageLabel.setText("");
                    }
                    break;
            }
        });
    }

    private void updateHistoryDisplay() {
        StringBuilder sb = new StringBuilder();
        var history = viewModel.getHistory();
        
        if (history == null || history.isEmpty()) {
            historyArea.setText("No search history available. Try searching for topics first.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (int i = 0; i < history.size(); i++) {
            SearchHistoryViewModel.HistoryItemVM item = history.get(i);
            sb.append(String.format("%d. %s\n", i + 1, item.getTopic()));
            if (item.getSearchedAt() != null) {
                sb.append(String.format("   Searched at: %s\n", 
                    item.getSearchedAt().format(formatter)));
            }
            sb.append("\n");
        }
        
        historyArea.setText(sb.toString());
    }

    public void setLoadHistoryController(LoadSearchHistoryController controller) {
        this.loadHistoryController = controller;
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}

