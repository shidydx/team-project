package view;

import entity.Article;
import interface_adapter.right_news_summary.RightNewsController;
import interface_adapter.right_news_summary.RightNewsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RightNewsSummaryView extends JPanel {
    public static final String VIEW_NAME = "Right News Summary";
    private final RightNewsController controller;
    private final RightNewsViewModel viewModel;

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

    public RightNewsSummaryView(RightNewsController controller, RightNewsViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel topicPanel = new JPanel(new BorderLayout());
        JLabel topicLabel = new JLabel("Topic: ");
        topicField = new JTextField();
        topicPanel.add(topicLabel, BorderLayout.WEST);
        topicPanel.add(topicField, BorderLayout.CENTER);
        JPanel summaryPanel = new JPanel(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary:");
        summaryArea = new JTextArea(8, 40);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setEditable(false);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryPanel.add(summaryLabel, BorderLayout.NORTH);
        summaryPanel.add(summaryScroll, BorderLayout.CENTER);
        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new GridLayout(4, 2, 4, 4));
        JLabel sourceLabel = new JLabel("Source:");
        sourceComboBox = new JComboBox<>();
        sourceComboBox.addItem("Right source");
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summarizeButton = new JButton("Summarize Right News");
        summarizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        summarizeButton.setBackground(Color.BLACK);
        summarizeButton.setForeground(Color.WHITE);
        summarizeButton.setOpaque(true);
        summarizeButton.setFocusPainted(false);
        
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setFocusPainted(false);
        
        JButton changeButton = new JButton("Switch to Left News");
        changeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        changeButton.setBackground(Color.BLACK);
        changeButton.setForeground(Color.WHITE);
        changeButton.setOpaque(true);
        changeButton.setFocusPainted(false);
        
        JButton comparisonButton = new JButton("Compare Coverage");
        comparisonButton.setFont(new Font("Arial", Font.PLAIN, 14));
        comparisonButton.setBackground(Color.BLACK);
        comparisonButton.setForeground(Color.WHITE);
        comparisonButton.setOpaque(true);
        comparisonButton.setFocusPainted(false);
        
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        
        buttonPanel.add(backButton);
        buttonPanel.add(summarizeButton);
        buttonPanel.add(changeButton);
        buttonPanel.add(comparisonButton);
        
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorLabel);
        
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
                errorLabel.setText(error);
                summaryArea.setText("");
                JOptionPane.showMessageDialog(
                        RightNewsSummaryView.this,
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
        backButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, "EnterTopicView");
            }
        });
        changeButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "left_news_summary");
        });
        comparisonButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "comparison");
        });
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public void setTopic(String topic) {
        topicField.setText(topic);
    }

    private void fillSourceComboBox() {
        sourceComboBox.removeAllItems();
        List<Article> articles = viewModel.getArticles();
        if (articles == null || articles.isEmpty()) {
            String label = viewModel.getName();
            if (label == null || label.isEmpty()) {
                label = "Right source";
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
        
        // Find article by source name matching
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
        
        // If no match found, use first article
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
        if (viewModel.getErrorMessage() != null && !viewModel.getErrorMessage().isEmpty()) {
            errorLabel.setText(viewModel.getErrorMessage());
            summaryArea.setText("");
        } else if (viewModel.getSummary() != null && !viewModel.getSummary().isEmpty()) {
            summaryArea.setText(viewModel.getSummary());
            errorLabel.setText("");
        }
    }

    public static void showInFrame(RightNewsController controller, RightNewsViewModel viewModel) {
        JFrame frame = new JFrame(VIEW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new RightNewsSummaryView(controller, viewModel));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
