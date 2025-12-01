package view;

import entity.Article;
import interface_adapter.ComparisonController;
import interface_adapter.comparison.ComparisonState;
import interface_adapter.comparison.ComparisonViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ComparisonView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = "comparison";

    private final ComparisonViewModel viewModel;
    private ComparisonController controller;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private final JTextField topicField;
    private final JButton compareButton;
    private final JButton backButton;
    private final JTextArea leftSummaryArea;
    private final JTextArea rightSummaryArea;
    private final JTextArea comparisonArea;
    private final JLabel statusLabel;
    private final JPanel contentPanel;

    public ComparisonView(ComparisonViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Compare News Coverage");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        JLabel topicLabel = new JLabel("Topic:");
        topicLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topicField = new JTextField();
        topicField.setFont(new Font("Arial", Font.PLAIN, 14));
        compareButton = new JButton("Generate Comparison");
        compareButton.setFont(new Font("Arial", Font.BOLD, 14));
        compareButton.setBackground(new Color(59, 89, 182));
        compareButton.setForeground(Color.WHITE);
        compareButton.setFocusPainted(false);
        
        inputPanel.add(topicLabel, BorderLayout.WEST);
        inputPanel.add(topicField, BorderLayout.CENTER);
        inputPanel.add(compareButton, BorderLayout.EAST);
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        contentPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        
        JPanel leftPanel = createSummaryPanel("Left-Leaning Perspective", new Color(52, 152, 219));
        leftSummaryArea = (JTextArea) ((JScrollPane) leftPanel.getComponent(0)).getViewport().getView();
        
        JPanel rightPanel = createSummaryPanel("Right-Leaning Perspective", new Color(231, 76, 60));
        rightSummaryArea = (JTextArea) ((JScrollPane) rightPanel.getComponent(0)).getViewport().getView();
        
        JPanel comparisonPanel = createSummaryPanel("Comparative Analysis", new Color(46, 204, 113));
        comparisonArea = (JTextArea) ((JScrollPane) comparisonPanel.getComponent(0)).getViewport().getView();
        
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        contentPanel.add(comparisonPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("← Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        bottomPanel.add(backButton);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        compareButton.addActionListener(e -> {
            String topic = topicField.getText().trim();
            if (!topic.isEmpty() && controller != null) {
                ComparisonState state = viewModel.getState();
                state.setTopic(topic);
                state.setLoading(true);
                state.setError("");
                
                statusLabel.setText("Loading comparison... This may take a moment.");
                statusLabel.setForeground(new Color(100, 100, 100));
                compareButton.setEnabled(false);
                
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        controller.execute(topic);
                        return null;
                    }
                    
                    @Override
                    protected void done() {
                        compareButton.setEnabled(true);
                    }
                }.execute();
            } else if (topic.isEmpty()) {
                statusLabel.setText("Please enter a topic to compare.");
                statusLabel.setForeground(Color.RED);
            }
        });

        backButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, RightNewsSummaryView.VIEW_NAME);
            }
        });
    }

    private JPanel createSummaryPanel(String title, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 2),
            title
        );
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        border.setTitleColor(borderColor);
        panel.setBorder(border);
        
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    public void setController(ComparisonController controller) {
        this.controller = controller;
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            updateView();
        }
    }

    private void updateView() {
        ComparisonState state = viewModel.getState();
        
        if (state.getError() != null && !state.getError().isEmpty()) {
            statusLabel.setText("Error: " + state.getError());
            statusLabel.setForeground(Color.RED);
            leftSummaryArea.setText("");
            rightSummaryArea.setText("");
            comparisonArea.setText("");
        } else if (!state.isLoading() && state.getComparisonAnalysis() != null && !state.getComparisonAnalysis().isEmpty()) {
            statusLabel.setText("Comparison completed successfully.");
            statusLabel.setForeground(new Color(46, 204, 113));
            leftSummaryArea.setText(state.getLeftSummary());
            rightSummaryArea.setText(state.getRightSummary());
            comparisonArea.setText(state.getComparisonAnalysis());
            
            leftSummaryArea.setCaretPosition(0);
            rightSummaryArea.setCaretPosition(0);
            comparisonArea.setCaretPosition(0);
        }
    }
}

