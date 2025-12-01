package view;

import entity.Article;
import interface_adapter.left_news_summary.LeftNewsSummaryController;
import interface_adapter.left_news_summary.LeftNewsSummaryState;
import interface_adapter.left_news_summary.LeftNewsSummaryViewModel;
import interface_adapter.savetopic.SaveTopicController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeftNewsSummaryView extends JPanel {
    public static final String VIEW_NAME = "left_news_summary";

    private SaveTopicController saveTopicController;

    public void setSaveTopicController(SaveTopicController saveTopicController) {
        this.saveTopicController = saveTopicController;
    }


    private final LeftNewsSummaryViewModel viewModel;
    private LeftNewsSummaryController controller;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private final JTextField topicField;
    private final JTextArea summaryArea;
    private final JComboBox<String> sourceComboBox;
    private final JTextField titleField;
    private final JTextField nameField;
    private final JTextField linkField;
    private final JButton summarizeButton;
    private final JLabel errorLabel;

    public LeftNewsSummaryView(LeftNewsSummaryViewModel viewModel) {
        this.viewModel = viewModel;
        this.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Topic panel
        JPanel topicPanel = new JPanel(new BorderLayout());
        JLabel topicLabel = new JLabel("Topic: ");
        topicField = new JTextField();
        topicPanel.add(topicLabel, BorderLayout.WEST);
        topicPanel.add(topicField, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary:");
        summaryArea = new JTextArea(8, 40);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setEditable(false);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryPanel.add(summaryLabel, BorderLayout.NORTH);
        summaryPanel.add(summaryScroll, BorderLayout.CENTER);


        // Source panel with article details
        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new GridLayout(4, 2, 4, 4));
        JLabel sourceLabel = new JLabel("Source:");
        sourceComboBox = new JComboBox<>();
        sourceComboBox.addItem("Left source");
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        titleField.setEditable(false);
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        nameField.setEditable(false);
        JLabel linkLabel = new JLabel("Link:");
        linkField = new JTextField();
        linkField.setEditable(false);
        sourcePanel.add(sourceLabel);
        sourcePanel.add(sourceComboBox);
        sourcePanel.add(titleLabel);
        sourcePanel.add(titleField);
        sourcePanel.add(nameLabel);
        sourcePanel.add(nameField);
        sourcePanel.add(linkLabel);
        sourcePanel.add(linkField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summarizeButton = new JButton("Summarize Left News");
        JButton searchHistoryButton = new JButton("View Search History");
        JButton switchToRightButton = new JButton("Switch to Right News Summary");
        JButton saveButton = new JButton("Add to Search History");
        buttonPanel.add(saveButton);
        buttonPanel.add(summarizeButton);
        buttonPanel.add(searchHistoryButton);
        buttonPanel.add(switchToRightButton);
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorLabel);
        
        // Add components to main panel
        mainPanel.add(topicPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(sourcePanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(errorPanel);
        
        this.add(mainPanel, BorderLayout.CENTER);
        
        // Set up listeners
        sourceComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateArticleDetails();
            }
        });
        
        summarizeButton.addActionListener(e -> {
            String keyword = topicField.getText().trim();
            if (controller != null) {
                controller.execute(keyword);
            }
            String error = viewModel.getErrorMessage();
            if (error != null && !error.isEmpty()) {
                JOptionPane.showMessageDialog(
                        LeftNewsSummaryView.this,
                        error,
                        "Source Unavailable",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            summaryArea.setText(viewModel.getSummary());
            linkField.setText(viewModel.getUrl());
            fillSourceComboBox();
            updateArticleDetails();
        });

        saveButton.addActionListener(e -> {
            if (saveTopicController == null) {
                System.err.println("SaveTopicController not set!");
                return;
            }

            String topic = topicField.getText().trim();
            if (!topic.isEmpty()) {
                saveTopicController.save(topic, "left");
                JOptionPane.showMessageDialog(
                        this,
                        "Topic added!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });



        searchHistoryButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, SearchHistoryView.VIEW_NAME);
            }
        });
        
        switchToRightButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, RightNewsSummaryView.VIEW_NAME);
            }
        });
    }

    public void setController(LeftNewsSummaryController controller) {
        this.controller = controller;
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void fillSourceComboBox() {
        sourceComboBox.removeAllItems();
        List<Article> articles = viewModel.getArticles();
        if (articles == null || articles.isEmpty()) {
            String label = viewModel.getName();
            if (label == null || label.isEmpty()) {
                label = "Left source";
            }
            sourceComboBox.addItem(label);
            if (viewModel.getTitle() == null) {
                titleField.setText("");
            } else {
                titleField.setText(viewModel.getTitle());
            }
            if (viewModel.getName() == null) {
                nameField.setText("");
            } else {
                nameField.setText(viewModel.getName());
            }
            if (viewModel.getUrl() == null) {
                linkField.setText("");
            } else {
                linkField.setText(viewModel.getUrl());
            }
            return;
        }
        Set<String> addedSources = new HashSet<>();
        for (Article article : articles) {
            String label = article.getSourceName();
            if (label == null || label.isEmpty()) {
                label = article.getTitle();
            }
            if (label == null || label.isEmpty()) {
                label = "Source";
            }
            // Only add if we haven't seen this source name before
            if (!addedSources.contains(label)) {
                sourceComboBox.addItem(label);
                addedSources.add(label);
            }
        }
        if (sourceComboBox.getItemCount() > 0) {
            sourceComboBox.setSelectedIndex(0);
        }
    }

    private void updateArticleDetails() {
        List<Article> articles = viewModel.getArticles();
        if (articles == null || articles.isEmpty()) {
            return;
        }
        
        String selectedSource = (String) sourceComboBox.getSelectedItem();
        if (selectedSource == null) {
            return;
        }
        
        // Find the first article that matches the selected source name
        Article article = null;
        for (Article a : articles) {
            String sourceName = a.getSourceName();
            if (sourceName == null || sourceName.isEmpty()) {
                sourceName = a.getTitle();
            }
            if (sourceName == null || sourceName.isEmpty()) {
                sourceName = "Source";
            }
            if (selectedSource.equals(sourceName)) {
                article = a;
                break;
            }
        }
        
        // Fallback to first article if no match found
        if (article == null && !articles.isEmpty()) {
            article = articles.get(0);
        }
        
        if (article != null) {
            if (article.getTitle() == null) {
                titleField.setText("");
            } else {
                titleField.setText(article.getTitle());
            }
            if (article.getSourceName() == null) {
                nameField.setText("");
            } else {
                nameField.setText(article.getSourceName());
            }
            if (article.getUrl() == null) {
                linkField.setText("");
            } else {
                linkField.setText(article.getUrl());
            }
        }
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

