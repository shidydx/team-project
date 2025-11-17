package view;

import entity.Article;
import interface_adapter.right_news_summary.RightNewsController;
import interface_adapter.right_news_summary.RightNewsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class RightNewsSummaryView extends JPanel {
    private final RightNewsController controller;
    private final RightNewsViewModel viewModel;
    private final JTextField topicField;
    private final JTextArea summaryArea;
    private final JComboBox<String> sourceComboBox;
    private final JTextField titleField;
    private final JTextField nameField;
    private final JTextField linkField;
    private final JButton summarizeButton;

    public RightNewsSummaryView(RightNewsController controller, RightNewsViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
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
        buttonPanel.add(summarizeButton);
        mainPanel.add(topicPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(sourcePanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);
        add(mainPanel, BorderLayout.CENTER);

        sourceComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateArticleDetails();
                }
            }
        });

        summarizeButton.addActionListener(e -> {
            String keyword = topicField.getText().trim();
            controller.execute(keyword);
            String error = viewModel.getErrorMessage();
            if (error != null && !error.isEmpty()) {
                JOptionPane.showMessageDialog(
                        RightNewsSummaryView.this,
                        error,
                        "Source Unavailable",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                summaryArea.setText(viewModel.getSummary());
                linkField.setText(viewModel.getUrl());
                fillSourceComboBox();
                updateArticleDetails();
            }
        });
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
            titleField.setText(viewModel.getTitle() == null ? "" : viewModel.getTitle());
            nameField.setText(viewModel.getName() == null ? "" : viewModel.getName());
            linkField.setText(viewModel.getUrl() == null ? "" : viewModel.getUrl());
            return;
        }
        for (Article article : articles) {
            String label = article.getSourceName();
            if (label == null || label.isEmpty()) {
                label = article.getTitle();
            }
            if (label == null || label.isEmpty()) {
                label = "Source";
            }
            sourceComboBox.addItem(label);
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
        int index = sourceComboBox.getSelectedIndex();
        if (index < 0 || index >= articles.size()) {
            index = 0;
        }
        Article article = articles.get(index);
        titleField.setText(article.getTitle() == null ? "" : article.getTitle());
        nameField.setText(article.getSourceName() == null ? "" : article.getSourceName());
        linkField.setText(article.getUrl() == null ? "" : article.getUrl());
    }

    public static void showInFrame(RightNewsController controller, RightNewsViewModel viewModel) {
        JFrame frame = new JFrame("Right source link");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new RightNewsSummaryView(controller, viewModel));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
