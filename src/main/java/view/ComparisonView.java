package view;

import interface_adapter.ComparisonController;
import interface_adapter.comparison.ComparisonState;
import interface_adapter.comparison.ComparisonViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

// ui for displaying left/right summaries and comparison analysis side by side
public class ComparisonView extends JPanel implements PropertyChangeListener {
    public static final String VIEW_NAME = "comparison";

    private final ComparisonViewModel viewModel;
    private ComparisonController controller;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private final JTextField topicField;
    private final JButton backButton;
    private final JButton saveTopicButton;
    private final JEditorPane leftSummaryArea;
    private final JEditorPane rightSummaryArea;
    private final JEditorPane comparisonArea;
    private final JLabel statusLabel;
    private final JPanel contentPanel;
    private interface_adapter.savetopic.SaveTopicController saveTopicController;

    // initialize comparison view with topic input, summary panels, and save functionality
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
        
        inputPanel.add(topicLabel, BorderLayout.WEST);
        inputPanel.add(topicField, BorderLayout.CENTER);
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        contentPanel = new JPanel(new BorderLayout(0, 15));
        
        JPanel summariesPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        summariesPanel.setPreferredSize(new Dimension(0, 300));
        
        JPanel leftPanel = createSummaryPanel("Left-Leaning Perspective", new Color(52, 152, 219));
        leftSummaryArea = (JEditorPane) ((JScrollPane) leftPanel.getComponent(0)).getViewport().getView();
        
        JPanel rightPanel = createSummaryPanel("Right-Leaning Perspective", new Color(231, 76, 60));
        rightSummaryArea = (JEditorPane) ((JScrollPane) rightPanel.getComponent(0)).getViewport().getView();
        
        summariesPanel.add(leftPanel);
        summariesPanel.add(rightPanel);
        
        JPanel comparisonPanel = createSummaryPanel("Comparative Analysis", new Color(46, 204, 113));
        comparisonArea = (JEditorPane) ((JScrollPane) comparisonPanel.getComponent(0)).getViewport().getView();
        
        contentPanel.add(summariesPanel, BorderLayout.NORTH);
        contentPanel.add(comparisonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setOpaque(true);
        backButton.setFocusPainted(false);
        
        saveTopicButton = new JButton("Save Topic & Summaries");
        saveTopicButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveTopicButton.setForeground(Color.BLACK);
        saveTopicButton.setOpaque(true);
        saveTopicButton.setFocusPainted(false);
        saveTopicButton.setToolTipText("Save this topic and all summaries (left, right, and comparison)");
        saveTopicButton.setVisible(false);
        
        bottomPanel.add(backButton);
        bottomPanel.add(saveTopicButton);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, "EnterTopicView");
            }
        });
        
        saveTopicButton.addActionListener(e -> {
            ComparisonState state = viewModel.getState();
            String topic = state.getTopic();
            String leftSummary = state.getLeftSummary();
            String rightSummary = state.getRightSummary();
            String comparisonSummary = state.getComparisonAnalysis();
            
            if (topic == null || topic.trim().isEmpty()) {
                statusLabel.setText("No topic to save. Please run a comparison first.");
                statusLabel.setForeground(Color.RED);
                return;
            }
            
            if (leftSummary == null || rightSummary == null || comparisonSummary == null ||
                leftSummary.trim().isEmpty() || rightSummary.trim().isEmpty() || comparisonSummary.trim().isEmpty()) {
                statusLabel.setText("Please wait for all summaries to load before saving.");
                statusLabel.setForeground(Color.RED);
                return;
            }
            
            if (saveTopicController != null) {
                saveTopicController.saveWithSummaries(topic.trim(), "default-user", 
                                                     leftSummary, rightSummary, comparisonSummary);
                statusLabel.setText("Topic and summaries saved successfully!");
                statusLabel.setForeground(new Color(46, 204, 113));
            } else {
                statusLabel.setText("Save feature not available.");
                statusLabel.setForeground(Color.RED);
            }
        });
    }
    
    public void setSaveTopicController(interface_adapter.savetopic.SaveTopicController controller) {
        this.saveTopicController = controller;
    }
    
    public void loadSavedTopic(entity.Topic topic) {
        if (topic != null) {
            topicField.setText(topic.getKeyword());
            ComparisonState state = viewModel.getState();
            state.setTopic(topic.getKeyword());
            state.setLeftSummary(topic.getLeftSummary());
            state.setRightSummary(topic.getRightSummary());
            state.setComparisonAnalysis(topic.getComparisonSummary());
            state.setLoading(false);
            state.setError("");
            viewModel.firePropertyChanged();
            
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, VIEW_NAME);
            }
        }
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
        
        JEditorPane textArea = new JEditorPane();
        textArea.setContentType("text/html");
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setBackground(new Color(250, 250, 250));
        textArea.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                } catch (Exception ex) {
                    System.err.println("Error opening link: " + ex.getMessage());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    public void setController(ComparisonController controller) {
        this.controller = controller;
    }

    public void triggerComparison(String topic) {
        topicField.setText(topic);
        runComparison(topic);
    }

    // execute comparison use case asynchronously to avoid blocking ui
    private void runComparison(String topic) {
        if (!topic.isEmpty() && controller != null) {
            ComparisonState state = viewModel.getState();
            state.setTopic(topic);
            state.setLoading(true);
            state.setError("");

            statusLabel.setText("Loading comparison... This may take a moment.");
            statusLabel.setForeground(new Color(100, 100, 100));
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    controller.execute(topic);
                    return null;
                }
            }.execute();
        } else if (topic.isEmpty()) {
            statusLabel.setText("Please enter a topic to compare.");
            statusLabel.setForeground(Color.RED);
        }
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void setTopic(String topic) {
        topicField.setText(topic);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            updateView();
        }
    }

    // update ui based on view model state: show errors or display summaries
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
            leftSummaryArea.setText(convertToHtml(state.getLeftSummary()));
            rightSummaryArea.setText(convertToHtml(state.getRightSummary()));
            comparisonArea.setText(convertToHtml(state.getComparisonAnalysis()));
            
            leftSummaryArea.setCaretPosition(0);
            rightSummaryArea.setCaretPosition(0);
            comparisonArea.setCaretPosition(0);
            
            saveTopicButton.setVisible(true);
        }
    }
    
    // convert plain text to html with link detection and formatting
    private String convertToHtml(String text) {
        if (text == null || text.isEmpty()) {
            return "<html><body></body></html>";
        }
        String html = text.replace("&", "&amp;")
                          .replace("<", "&lt;")
                          .replace(">", "&gt;");
        
        html = html.replaceAll("(https?://[^\\s]+)", "<a href=\"$1\">$1</a>");
        
        html = html.replace("\n", "<br>");
        
        return "<html><body style='font-family: Arial; font-size: 13px; padding: 10px; color: #000000;'>" + html + "</body></html>";
    }
}

