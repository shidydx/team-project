package view;

import interface_adapter.entertopic.EnterTopicController;
import interface_adapter.entertopic.EnterTopicState;
import interface_adapter.entertopic.EnterTopicViewModel;
import interface_adapter.left_news_summary.LeftNewsSummaryController;
import interface_adapter.right_news_summary.RightNewsController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.net.URI;

public class EnterTopicView extends JPanel {
    private static final String VIEW_NAME = "EnterTopicView";
    private final EnterTopicViewModel viewModel;

    private EnterTopicController controller;
    private LeftNewsSummaryController leftController;
    private RightNewsController rightController;
    private interface_adapter.left_news_summary.LeftNewsSummaryViewModel leftViewModel;
    private interface_adapter.right_news_summary.RightNewsViewModel rightViewModel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ComparisonView comparisonView;

    private final JLabel errorLabel = new JLabel();
    private final JTextField topicTextField = new JTextField();
    private final JButton searchButton = new JButton("Search & Summarize");
    private final JButton comparisonButton = new JButton("View Detailed Comparison");
    
    private final JEditorPane leftSummaryArea;
    private final JEditorPane rightSummaryArea;
    private final JPanel summariesPanel = new JPanel();


    public EnterTopicView(EnterTopicViewModel viewModel) {
        this.viewModel = viewModel;
        
        leftSummaryArea = createEditorPane();
        rightSummaryArea = createEditorPane();
        initializeUI();
        updateView();
    }
    
    private JEditorPane createEditorPane() {
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        pane.setFont(new Font("Arial", Font.PLAIN, 13));
        pane.setBackground(new Color(250, 250, 250));
        pane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                } catch (Exception ex) {
                    System.err.println("Error opening link: " + ex.getMessage());
                }
            }
        });
        return pane;
    }

    public void setController(EnterTopicController controller) {
        this.controller = controller;
    }

    public void setLeftController(LeftNewsSummaryController leftController, 
                                   interface_adapter.left_news_summary.LeftNewsSummaryViewModel leftViewModel) {
        this.leftController = leftController;
        this.leftViewModel = leftViewModel;
    }

    public void setRightController(RightNewsController rightController,
                                    interface_adapter.right_news_summary.RightNewsViewModel rightViewModel) {
        this.rightController = rightController;
        this.rightViewModel = rightViewModel;
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public void setComparisonView(ComparisonView comparisonView) {
        this.comparisonView = comparisonView;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("MiddleGround AI - Compare Perspectives");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        JLabel topicLabel = new JLabel("Topic:");
        topicLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topicTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(59, 89, 182));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        
        searchPanel.add(topicLabel, BorderLayout.WEST);
        searchPanel.add(topicTextField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(errorLabel, BorderLayout.SOUTH);

        
        summariesPanel.setLayout(new GridLayout(1, 2, 15, 0));
        
        
        JPanel leftPanel = createSummaryPanel("Left-Leaning Perspective", leftSummaryArea, new Color(52, 152, 219));
        
        
        JPanel rightPanel = createSummaryPanel("Right-Leaning Perspective", rightSummaryArea, new Color(231, 76, 60));
        
        summariesPanel.add(leftPanel);
        summariesPanel.add(rightPanel);
        summariesPanel.setVisible(false);

        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comparisonButton.setFont(new Font("Arial", Font.BOLD, 16));
        comparisonButton.setBackground(new Color(46, 204, 113));
        comparisonButton.setForeground(Color.WHITE);
        comparisonButton.setFocusPainted(false);
        comparisonButton.setVisible(false);
        bottomPanel.add(comparisonButton);

        
        searchButton.addActionListener(e -> {
            String topic = topicTextField.getText().trim();
            
            if (topic.isEmpty()) {
                errorLabel.setText("Please enter a topic");
                summariesPanel.setVisible(false);
                comparisonButton.setVisible(false);
                return;
            }

            
            if (controller != null) {
                controller.execute(topic);
                EnterTopicState state = viewModel.getState();
                String errorMsg = state.getErrorMessage();

                if (errorMsg != null && !errorMsg.isEmpty()) {
                    errorLabel.setText(errorMsg);
                    summariesPanel.setVisible(false);
                    comparisonButton.setVisible(false);
                    return;
                }
            }

            errorLabel.setText("Loading summaries...");
            leftSummaryArea.setText(convertToHtml("Fetching left-leaning news..."));
            rightSummaryArea.setText(convertToHtml("Fetching right-leaning news..."));
            summariesPanel.setVisible(true);
            comparisonButton.setVisible(false);

            
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    if (leftController != null) {
                        leftController.execute(topic);
                    }
                    if (rightController != null) {
                        rightController.execute(topic);
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    
                    if (leftViewModel != null) {
                        String leftSummary = leftViewModel.getSummary();
                        String leftError = leftViewModel.getErrorMessage();
                        if (leftError != null && !leftError.isEmpty()) {
                            leftSummaryArea.setText(convertToHtml("Error: " + leftError));
                        } else if (leftSummary != null && !leftSummary.isEmpty()) {
                            leftSummaryArea.setText(convertToHtml(leftSummary));
                        } else {
                            leftSummaryArea.setText(convertToHtml("No summary available"));
                        }
                    }
                    
                    if (rightViewModel != null) {
                        String rightSummary = rightViewModel.getSummary();
                        String rightError = rightViewModel.getErrorMessage();
                        if (rightError != null && !rightError.isEmpty()) {
                            rightSummaryArea.setText(convertToHtml("Error: " + rightError));
                        } else if (rightSummary != null && !rightSummary.isEmpty()) {
                            rightSummaryArea.setText(convertToHtml(rightSummary));
                        } else {
                            rightSummaryArea.setText(convertToHtml("No summary available"));
                        }
                    }
                    
                    errorLabel.setText("Summaries loaded!");
                    errorLabel.setForeground(new Color(46, 204, 113));
                    comparisonButton.setVisible(true);
                    
                    leftSummaryArea.setCaretPosition(0);
                    rightSummaryArea.setCaretPosition(0);
                }
            }.execute();
        });

        comparisonButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                String topic = topicTextField.getText().trim();
                if (topic.isEmpty()) {
                    errorLabel.setText("Please enter a topic");
                    return;
                }
                if (comparisonView != null) {
                    comparisonView.triggerComparison(topic);
                }
                cardLayout.show(cardPanel, comparisonView != null
                        ? comparisonView.getViewName()
                        : "comparison");
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(summariesPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSummaryPanel(String title, JEditorPane textArea, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 2),
            title
        );
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        border.setTitleColor(borderColor);
        panel.setBorder(border);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private String convertToHtml(String text) {
        if (text == null || text.isEmpty()) {
            return "<html><body></body></html>";
        }
        
        
        String html = text.replace("&", "&amp;")
                          .replace("<", "&lt;")
                          .replace(">", "&gt;");
        
        html = html.replaceAll("(https?://[^\\s]+)", "<a href=\"$1\">$1</a>");
        
        html = html.replace("\n", "<br>");
        
        return "<html><body style='font-family: Arial; font-size: 13px; padding: 10px;'>" + html + "</body></html>";
    }
    public void updateView() {
        if (viewModel.getState().getErrorMessage() != null && !viewModel.getState().getErrorMessage().isEmpty()) {
            errorLabel.setText(viewModel.getState().getErrorMessage());
            errorLabel.setForeground(Color.RED);
        } else if (viewModel.getState().getTopic() != null && !viewModel.getState().getTopic().isEmpty()) {
            topicTextField.setText(viewModel.getState().getTopic());
        }
    }

    public void updateLeftSummary(String summary) {
        leftSummaryArea.setText(convertToHtml(summary));
        leftSummaryArea.setCaretPosition(0);
    }

    public void updateRightSummary(String summary) {
        rightSummaryArea.setText(convertToHtml(summary));
        rightSummaryArea.setCaretPosition(0);
    }
}
