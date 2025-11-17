package view;

import interface_adapter.ComparisonController;
import interface_adapter.compare_summaries.ComparisonState;
import interface_adapter.compare_summaries.ComparisonViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ComparisonView extends JPanel {
    public static final String VIEW_NAME = "comparison";
    private final ComparisonViewModel viewModel;
    private ComparisonController controller;
    private interface_adapter.left_news_summary.LeftNewsSummaryController leftSummaryController;
    private interface_adapter.left_news_summary.LeftNewsSummaryViewModel leftSummaryViewModel;

    private final JTextField topicField = new JTextField(20);
    private final JTextArea leftSummaryInput = new JTextArea(5, 40);
    private final JTextArea rightSummaryInput = new JTextArea(5, 40);
    private final JButton compareButton = new JButton("Compare Summaries");
    private final JButton fetchLeftSummaryButton = new JButton("Fetch Left Summary");
    
    private final JTextArea analysisArea = new JTextArea(15, 80);
    private final JLabel errorLabel = new JLabel();

    public ComparisonView(ComparisonViewModel viewModel) {
        this.viewModel = viewModel;
        initializeUI();
        viewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Property change event received: " + evt.getPropertyName());
                System.out.println("Old value: " + evt.getOldValue());
                System.out.println("New value: " + evt.getNewValue());
                updateView();
            }
        });
    }

    public void setController(ComparisonController controller) {
        this.controller = controller;
    }
    
    public void setLeftSummaryController(interface_adapter.left_news_summary.LeftNewsSummaryController controller,
                                        interface_adapter.left_news_summary.LeftNewsSummaryViewModel viewModel) {
        this.leftSummaryController = controller;
        this.leftSummaryViewModel = viewModel;
        // Listen for left summary updates
        viewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Update UI on EDT
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        interface_adapter.left_news_summary.LeftNewsSummaryState state = viewModel.getState();
                        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                            JOptionPane.showMessageDialog(ComparisonView.this, 
                                "Error fetching left summary: " + state.getErrorMessage());
                            fetchLeftSummaryButton.setEnabled(true);
                            fetchLeftSummaryButton.setText("Fetch Left Summary");
                        } else if (state.getSummary() != null && !state.getSummary().isEmpty()) {
                            leftSummaryInput.setText(state.getSummary());
                            fetchLeftSummaryButton.setEnabled(true);
                            fetchLeftSummaryButton.setText("Fetch Left Summary");
                        }
                    }
                });
            }
        });
    }
    
    public void setLeftSummaryAndTopic(String summary, String topic) {
        leftSummaryInput.setText(summary);
        topicField.setText(topic);
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Input panel at the top
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Summaries to Compare"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Topic:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        topicField.setText("Climate Change Policy"); // Default test topic
        inputPanel.add(topicField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        inputPanel.add(new JLabel("Left Summary:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        leftSummaryInput.setLineWrap(true);
        leftSummaryInput.setWrapStyleWord(true);
        JPanel leftSummaryPanel = new JPanel(new BorderLayout());
        leftSummaryPanel.add(new JScrollPane(leftSummaryInput), BorderLayout.CENTER);
        JPanel leftButtonPanel = new JPanel(new FlowLayout());
        JButton generateLeftButton = new JButton("Generate Sample Left Summary");
        leftButtonPanel.add(fetchLeftSummaryButton);
        leftButtonPanel.add(generateLeftButton);
        leftSummaryPanel.add(leftButtonPanel, BorderLayout.SOUTH);
        inputPanel.add(leftSummaryPanel, gbc);
        
        // Add action listener to fetch left summary button
        fetchLeftSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topic = topicField.getText().trim();
                if (topic.isEmpty()) {
                    JOptionPane.showMessageDialog(ComparisonView.this, 
                        "Please enter a topic first");
                    return;
                }
                if (leftSummaryController != null) {
                    fetchLeftSummaryButton.setEnabled(false);
                    fetchLeftSummaryButton.setText("Fetching...");
                    // Clear previous summary and error
                    leftSummaryInput.setText("Fetching news articles and generating summary... This may take 30-60 seconds.");
                    // Execute on a background thread to avoid blocking UI
                    new Thread(() -> {
                        try {
                            long startTime = System.currentTimeMillis();
                            leftSummaryController.execute(topic);
                            long elapsed = System.currentTimeMillis() - startTime;
                            System.out.println("Left summary fetch completed in " + elapsed + " ms");
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(ComparisonView.this, 
                                    "Error: " + ex.getMessage());
                                leftSummaryInput.setText("");
                                fetchLeftSummaryButton.setEnabled(true);
                                fetchLeftSummaryButton.setText("Fetch Left Summary");
                            });
                        }
                    }).start();
                } else {
                    JOptionPane.showMessageDialog(ComparisonView.this, 
                        "Left summary controller not available");
                }
            }
        });
        
        // Add action listener to generate sample left summary
        generateLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topic = topicField.getText().trim();
                if (topic.isEmpty()) {
                    JOptionPane.showMessageDialog(ComparisonView.this, 
                        "Please enter a topic first");
                    return;
                }
                // Generate a hardcoded sample left summary based on topic
                String sampleLeftSummary = generateSampleLeftSummary(topic);
                leftSummaryInput.setText(sampleLeftSummary);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        inputPanel.add(new JLabel("Right Summary:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        rightSummaryInput.setLineWrap(true);
        rightSummaryInput.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(rightSummaryInput), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generateRightButton = new JButton("Generate Sample Right Summary");
        buttonPanel.add(compareButton);
        buttonPanel.add(generateRightButton);
        inputPanel.add(buttonPanel, gbc);
        
        // Add action listener to generate sample right summary
        generateRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topic = topicField.getText().trim();
                if (topic.isEmpty()) {
                    JOptionPane.showMessageDialog(ComparisonView.this, 
                        "Please enter a topic first");
                    return;
                }
                // Generate a hardcoded sample right summary based on topic
                String sampleRightSummary = generateSampleRightSummary(topic);
                rightSummaryInput.setText(sampleRightSummary);
            }
        });

        // Add action listener to compare button
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    String topic = topicField.getText().trim();
                    String leftSummary = leftSummaryInput.getText().trim();
                    String rightSummary = rightSummaryInput.getText().trim();

                    if (topic.isEmpty() || leftSummary.isEmpty() || rightSummary.isEmpty()) {
                        JOptionPane.showMessageDialog(ComparisonView.this, 
                            "Please fill in all fields (topic, left summary, and right summary)");
                        return;
                    }

                    // Disable button and show loading state
                    compareButton.setEnabled(false);
                    compareButton.setText("Comparing...");
                    analysisArea.setText("Generating comparison analysis... This may take 30-60 seconds.");
                    
                    // Execute on background thread to avoid blocking UI
                    new Thread(() -> {
                        try {
                            long startTime = System.currentTimeMillis();
                            controller.execute(leftSummary, rightSummary, topic);
                            long elapsed = System.currentTimeMillis() - startTime;
                            System.out.println("Comparison completed in " + elapsed + " ms");
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(ComparisonView.this, 
                                    "Error: " + ex.getMessage());
                                compareButton.setEnabled(true);
                                compareButton.setText("Compare Summaries");
                                analysisArea.setText("");
                            });
                        }
                    }).start();
                }
            }
        });

        // Results panel at the bottom - just the comparison analysis
        JPanel analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBorder(BorderFactory.createTitledBorder("Comparison Analysis"));
        analysisArea.setEditable(false);
        analysisArea.setWrapStyleWord(true);
        analysisArea.setLineWrap(true);
        analysisArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        JScrollPane analysisScrollPane = new JScrollPane(analysisArea);
        analysisPanel.add(analysisScrollPane, BorderLayout.CENTER);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(analysisPanel, BorderLayout.CENTER);
        bottomPanel.add(errorPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
    }

    public void updateView() {
        ComparisonState state = viewModel.getState();

        // Update UI on EDT
        SwingUtilities.invokeLater(() -> {
            if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                System.out.println("Comparison error: " + state.getErrorMessage());
                errorLabel.setText(state.getErrorMessage());
                analysisArea.setText("");
                compareButton.setEnabled(true);
                compareButton.setText("Compare Summaries");
            } else {
                String analysis = state.getComparisonAnalysis();
                System.out.println("Updating comparison analysis. Length: " + 
                    (analysis != null ? analysis.length() : 0));
                if (analysis != null && !analysis.isEmpty()) {
                    analysisArea.setText(analysis);
                    System.out.println("Comparison analysis displayed successfully");
                } else {
                    System.out.println("Warning: Comparison analysis is null or empty");
                    analysisArea.setText("No analysis available.");
                }
                errorLabel.setText("");
                compareButton.setEnabled(true);
                compareButton.setText("Compare Summaries");
            }
        });
    }
    
    private String generateSampleLeftSummary(String topic) {
        // Generate a hardcoded sample left-leaning summary based on the topic
        String lowerTopic = topic.toLowerCase();
        
        if (lowerTopic.contains("climate") || lowerTopic.contains("environment")) {
            return "Progressive perspectives on climate change policy call for immediate and aggressive action " +
                   "to address the climate crisis. Advocates argue that comprehensive government intervention is " +
                   "essential to reduce carbon emissions, transition to renewable energy, and protect vulnerable " +
                   "communities disproportionately affected by environmental degradation. They support policies such " +
                   "as the Green New Deal, carbon pricing, and strict emissions regulations on corporations. This " +
                   "approach prioritizes environmental justice and views climate action as both an environmental " +
                   "imperative and an opportunity to create green jobs and economic equity. Supporters emphasize " +
                   "that the costs of inaction far exceed the costs of implementing bold climate policies, and " +
                   "that the transition to clean energy can drive economic growth while protecting the planet.";
        } else if (lowerTopic.contains("healthcare") || lowerTopic.contains("health")) {
            return "Progressive healthcare policy advocates for universal healthcare coverage, arguing that access " +
                   "to healthcare is a fundamental human right. Supporters of Medicare for All and similar programs " +
                   "contend that a single-payer system would reduce overall healthcare costs, eliminate insurance " +
                   "company profits, and ensure that all Americans receive quality care regardless of income. They " +
                   "argue that the current system leaves millions uninsured or underinsured, leading to preventable " +
                   "suffering and financial hardship. This approach emphasizes that healthcare should be treated as " +
                   "a public good rather than a commodity, and that government has a responsibility to ensure " +
                   "universal access. Supporters point to other developed nations with universal healthcare systems " +
                   "that achieve better health outcomes at lower costs.";
        } else if (lowerTopic.contains("education")) {
            return "Progressive education policy focuses on equitable funding, reducing achievement gaps, and " +
                   "expanding access to quality education for all students regardless of socioeconomic background. " +
                   "Advocates call for increased federal investment in public schools, universal pre-K programs, " +
                   "and free or reduced-cost college tuition. They argue that education is the great equalizer and " +
                   "that systemic inequities in school funding perpetuate cycles of poverty and inequality. This " +
                   "approach emphasizes addressing the root causes of educational disparities, including poverty, " +
                   "housing instability, and lack of resources in underserved communities. Supporters believe that " +
                   "robust public investment in education is essential for both individual opportunity and national " +
                   "economic competitiveness, and that all students deserve access to well-funded schools with " +
                   "qualified teachers and modern resources.";
        } else {
            // Generic progressive perspective
            return "Progressive perspectives on " + topic + " emphasize the role of government in addressing " +
                   "systemic inequalities and ensuring social justice. Advocates argue that proactive government " +
                   "intervention is necessary to protect vulnerable populations, regulate corporate power, and " +
                   "ensure equitable access to opportunities. They support policies that prioritize collective " +
                   "wellbeing, environmental protection, and economic fairness. This viewpoint emphasizes that " +
                   "addressing challenges related to " + topic + " requires comprehensive solutions that consider " +
                   "the needs of marginalized communities and prioritize long-term societal benefits over short-term " +
                   "corporate profits.";
        }
    }
    
    private String generateSampleRightSummary(String topic) {
        // Generate a hardcoded sample right-leaning summary based on the topic
        String lowerTopic = topic.toLowerCase();
        
        if (lowerTopic.contains("climate") || lowerTopic.contains("environment")) {
            return "Conservative perspectives on climate change policy emphasize market-based solutions " +
                   "and technological innovation over government mandates. Critics of aggressive climate " +
                   "regulations argue that such policies would significantly harm the economy, raise energy " +
                   "costs for families, and reduce American competitiveness. They advocate for investing in " +
                   "clean energy technology through private sector innovation rather than imposing carbon taxes " +
                   "or emissions caps. This approach prioritizes economic growth and energy independence while " +
                   "encouraging voluntary environmental stewardship. Supporters point to American innovation in " +
                   "natural gas and nuclear energy as examples of market-driven solutions that reduce emissions " +
                   "without sacrificing economic prosperity.";
        } else if (lowerTopic.contains("healthcare") || lowerTopic.contains("health")) {
            return "Conservative healthcare policy focuses on increasing competition, reducing costs through " +
                   "market forces, and preserving individual choice. Critics of government-run healthcare systems " +
                   "argue they lead to longer wait times, reduced quality of care, and higher taxes. They advocate " +
                   "for health savings accounts, allowing insurance sales across state lines, and reducing regulatory " +
                   "barriers that drive up costs. This approach emphasizes personal responsibility and consumer " +
                   "choice while opposing mandates that force individuals to purchase specific types of coverage. " +
                   "Supporters believe free-market competition will drive innovation and lower prices more effectively " +
                   "than government intervention.";
        } else if (lowerTopic.contains("education")) {
            return "Conservative education policy emphasizes school choice, parental rights, and local control " +
                   "over federal mandates. Critics of centralized education systems argue that one-size-fits-all " +
                   "approaches fail to meet diverse student needs and undermine local communities' ability to " +
                   "tailor education to their values. They advocate for charter schools, voucher programs, and " +
                   "homeschooling options that give parents more control over their children's education. This " +
                   "approach prioritizes accountability through competition and opposes federal overreach in " +
                   "curriculum decisions. Supporters believe that empowering parents and local communities will " +
                   "improve educational outcomes more effectively than increasing federal funding and regulations.";
        } else {
            // Generic conservative perspective
            return "Conservative perspectives on " + topic + " emphasize limited government intervention, " +
                   "fiscal responsibility, and free-market solutions. Critics argue that excessive regulation " +
                   "and government spending hinder economic growth and individual freedom. They advocate for " +
                   "private sector innovation and reduced bureaucratic oversight as the most effective approach " +
                   "to addressing challenges related to " + topic + ". This viewpoint prioritizes personal " +
                   "responsibility and market-driven outcomes over government mandates.";
        }
    }
}

