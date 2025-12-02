package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;

import interface_adapter.enter_topic.EnterTopicController;
import interface_adapter.enter_topic.EnterTopicState;
import interface_adapter.enter_topic.EnterTopicViewModel;
import interface_adapter.left_news_summary.LeftNewsSummaryController;
import interface_adapter.right_news_summary.RightNewsController;

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
    private final JButton viewSavedTopicsButton = new JButton("View Saved Topics");
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
        final JEditorPane pane = new JEditorPane();
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

    public void setLeftController(LeftNewsSummaryController lController,
                                   interface_adapter.left_news_summary.LeftNewsSummaryViewModel lViewModel) {
        this.leftController = lController;
        this.leftViewModel = lViewModel;
    }

    public void setRightController(RightNewsController rController,
                                    interface_adapter.right_news_summary.RightNewsViewModel rViewModel) {
        this.rightController = rController;
        this.rightViewModel = rViewModel;
    }

    public void setCardChange(CardLayout layout, JPanel panel) {
        this.cardLayout = layout;
        this.cardPanel = panel;
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
        searchButton.setFont(new Font("Arial", Font.PLAIN, 14));
        searchButton.setForeground(Color.BLACK);
        searchButton.setOpaque(true);
        searchButton.setFocusPainted(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        viewSavedTopicsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewSavedTopicsButton.setForeground(Color.BLACK);
        viewSavedTopicsButton.setOpaque(true);
        viewSavedTopicsButton.setFocusPainted(false);
        viewSavedTopicsButton.setToolTipText("View and manage your saved topics");
        
        buttonPanel.add(viewSavedTopicsButton);
        buttonPanel.add(searchButton);
        
        searchPanel.add(topicLabel, BorderLayout.WEST);
        searchPanel.add(topicTextField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        
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

        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comparisonButton.setFont(new Font("Arial", Font.PLAIN, 16));
        comparisonButton.setForeground(Color.BLACK);
        comparisonButton.setOpaque(true);
        comparisonButton.setFocusPainted(false);
        comparisonButton.setVisible(false);
        bottomPanel.add(comparisonButton);

        searchButton.addActionListener(e -> {
            final String topic = topicTextField.getText().trim();
            
            if (topic.isEmpty()) {
                errorLabel.setText("Please enter a topic");
                summariesPanel.setVisible(false);
                comparisonButton.setVisible(false);
                return;
            }
            
            if (controller != null) {
                controller.execute(topic);
                final EnterTopicState state = viewModel.getState();
                final String errorMsg = state.getErrorMessage();

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
                        final String leftSummary = leftViewModel.getSummary();
                        final String leftError = leftViewModel.getErrorMessage();
                        if (leftError != null && !leftError.isEmpty()) {
                            leftSummaryArea.setText(convertToHtml("Error: " + leftError));
                        }
                        else if (leftSummary != null && !leftSummary.isEmpty()) {
                            leftSummaryArea.setText(convertToHtml(leftSummary));
                        }
                        else {
                            leftSummaryArea.setText(convertToHtml("No summary available"));
                        }
                    }
                    
                    if (rightViewModel != null) {
                        final String rightSummary = rightViewModel.getSummary();
                        final String rightError = rightViewModel.getErrorMessage();
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
        viewSavedTopicsButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, "saved_topics");
            }
        });
        comparisonButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                final String topic = topicTextField.getText().trim();
                if (topic.isEmpty()) {
                    errorLabel.setText("Please enter a topic");
                    return;
                }
                if (comparisonView != null) {
                    comparisonView.triggerComparison(topic);
                }
                // cardLayout.show(cardPanel, comparisonView != null
                //       ? comparisonView.getViewName()
                //       : "comparison");
                if (comparisonView != null) {
                    cardLayout.show(cardPanel, comparisonView.getViewName());
                }
                else {
                    cardLayout.show(cardPanel, "comparison");
                }
            }
        });
        add(topPanel, BorderLayout.NORTH);
        add(summariesPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSummaryPanel(String title, JEditorPane textArea, Color borderColor) {
        final JPanel panel = new JPanel(new BorderLayout());
        
        final TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 2),
            title
        );
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        border.setTitleColor(borderColor);
        panel.setBorder(border);
        
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private String convertToHtml(String text) {
        String result;
        if (text == null || text.isEmpty()) {
            result = "<html><body></body></html>";
        }
        String html = text.replace("&", "&amp;")
                          .replace("<", "&lt;")
                          .replace(">", "&gt;");
        
        html = html.replaceAll("(https?://[^\\s]+)", "<a href=\"$1\">$1</a>");
        html = html.replace("\n", "<br>");
        result = "<html><body style='font-family: Arial; font-size: 13px; padding: 10px; color: #000000;'>"
                + html + "</body></html>";
        return result;
    }

    public void updateView() {
        if (viewModel.getState().getErrorMessage() != null && !viewModel.getState().getErrorMessage().isEmpty()) {
            errorLabel.setText(viewModel.getState().getErrorMessage());
            errorLabel.setForeground(Color.RED);
        }
        else if (viewModel.getState().getTopic() != null && !viewModel.getState().getTopic().isEmpty()) {
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
