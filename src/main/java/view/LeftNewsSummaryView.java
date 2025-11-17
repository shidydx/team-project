package view;

import interface_adapter.left_news_summary.LeftNewsSummaryController;
import interface_adapter.left_news_summary.LeftNewsSummaryState;
import interface_adapter.left_news_summary.LeftNewsSummaryViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LeftNewsSummaryView extends JPanel {
    public static final String VIEW_NAME = "left_news_summary";
    private final LeftNewsSummaryViewModel viewModel;
    private LeftNewsSummaryController controller;
    private view.ComparisonView comparisonView; // Reference to comparison view for copying summary

    private final JTextField topicField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private final JButton useInComparisonButton = new JButton("Use in Comparison");
    private final JTextArea summaryArea = new JTextArea(15, 40);
    private final JLabel errorLabel = new JLabel();

    public LeftNewsSummaryView(LeftNewsSummaryViewModel viewModel) {
        this.viewModel = viewModel;
        initializeUI();
        viewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateView();
            }
        });
    }

    public void setController(LeftNewsSummaryController controller) {
        this.controller = controller;
    }
    
    public void setComparisonView(view.ComparisonView comparisonView) {
        this.comparisonView = comparisonView;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Input panel at the top
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Search Topic"));
        inputPanel.add(new JLabel("Topic:"));
        inputPanel.add(topicField);
        inputPanel.add(searchButton);
        inputPanel.add(useInComparisonButton);
        
        // Initially disable the "Use in Comparison" button
        useInComparisonButton.setEnabled(false);

        // Add action listener to search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    String topic = topicField.getText().trim();
                    if (topic.isEmpty()) {
                        JOptionPane.showMessageDialog(LeftNewsSummaryView.this, 
                            "Please enter a topic to search");
                        return;
                    }
                    controller.execute(topic);
                }
            }
        });

        // Allow Enter key to trigger search
        topicField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButton.doClick();
            }
        });
        
        // Add action listener to "Use in Comparison" button
        useInComparisonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comparisonView != null) {
                    String summary = summaryArea.getText();
                    String topic = topicField.getText();
                    if (!summary.isEmpty() && !topic.isEmpty()) {
                        comparisonView.setLeftSummaryAndTopic(summary, topic);
                        JOptionPane.showMessageDialog(LeftNewsSummaryView.this, 
                            "Left summary copied to Comparison view! Navigate to 'Compare Summaries' to use it.");
                    }
                }
            }
        });

        // Summary panel in the center
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Left-Leaning News Summary"));
        summaryArea.setEditable(false);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setLineWrap(true);
        summaryArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        // Error panel at the bottom
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);
        add(errorPanel, BorderLayout.SOUTH);
    }
    
    public void updateView() {
        LeftNewsSummaryState state = viewModel.getState();
        
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            errorLabel.setText(state.getErrorMessage());
            summaryArea.setText("");
            useInComparisonButton.setEnabled(false);
        } else if (state.getSummary() != null && !state.getSummary().isEmpty()) {
            summaryArea.setText(state.getSummary());
            errorLabel.setText("");
            useInComparisonButton.setEnabled(true);
        }
    }
}

