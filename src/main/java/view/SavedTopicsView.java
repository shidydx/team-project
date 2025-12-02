package view;

import interface_adapter.delete_saved_topic.DeleteSavedTopicController;
import interface_adapter.delete_saved_topic.DeleteSavedTopicViewModel;
import interface_adapter.filter_saved_topic.FilterSavedTopicController;
import interface_adapter.filter_saved_topic.FilterSavedTopicViewModel;
import data_access.SavedTopicRepositoryImpl;
import entity.Topic;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// ui for viewing, filtering, and deleting saved topics
public class SavedTopicsView extends JPanel {
    public static final String VIEW_NAME = "saved_topics";

    private DeleteSavedTopicController deleteTopicController;
    private FilterSavedTopicController filterTopicController;
    private DeleteSavedTopicViewModel deleteTopicViewModel;
    private FilterSavedTopicViewModel filterTopicViewModel;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JList<String> savedTopicsList;
    private JButton loadSavedTopicsButton;
    private JButton backButton;
    private JLabel messageLabel;
    
    private JTextField topicInputField;
    private JButton filterButton;
    private JButton deleteButton;
    private JLabel filterResultLabel;
    private JLabel deleteResultLabel;
    
    private SavedTopicRepositoryImpl savedTopicRepository;
    private ComparisonView comparisonView;

    public SavedTopicsView() {
        initializeUI();
    }

    // set up ui components: topic list, filter/delete controls, and navigation
    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel savedTopicsPanel = new JPanel(new BorderLayout());
        savedTopicsPanel.setBorder(BorderFactory.createTitledBorder("Saved Topics (Click to Load)"));
        savedTopicsList = new JList<>();
        savedTopicsList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        savedTopicsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        savedTopicsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && savedTopicsList.getSelectedValue() != null) {
                String selectedTopic = savedTopicsList.getSelectedValue();
                loadSavedTopic(selectedTopic);
            }
        });
        JScrollPane savedTopicsScrollPane = new JScrollPane(savedTopicsList);
        savedTopicsPanel.add(savedTopicsScrollPane, BorderLayout.CENTER);
        JPanel savedTopicsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadSavedTopicsButton = new JButton("Refresh Saved Topics");
        loadSavedTopicsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loadSavedTopicsButton.setBackground(Color.BLACK);
        loadSavedTopicsButton.setForeground(Color.WHITE);
        loadSavedTopicsButton.setOpaque(true);
        loadSavedTopicsButton.setFocusPainted(false);
        loadSavedTopicsButton.addActionListener(e -> refreshSavedTopics());
        savedTopicsButtonPanel.add(loadSavedTopicsButton);
        savedTopicsPanel.add(savedTopicsButtonPanel, BorderLayout.SOUTH);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        messagePanel.add(messageLabel);

        JPanel managePanel = new JPanel(new BorderLayout());
        managePanel.setBorder(BorderFactory.createTitledBorder("Manage Saved Topics (Delete/Filter)"));
        managePanel.setPreferredSize(new Dimension(0, 130));
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Topic:"));
        topicInputField = new JTextField(20);
        inputPanel.add(topicInputField);
        
        filterButton = new JButton("Filter Topic");
        filterButton.setFont(new Font("Arial", Font.PLAIN, 14));
        filterButton.setBackground(Color.BLACK);
        filterButton.setForeground(Color.WHITE);
        filterButton.setOpaque(true);
        filterButton.setFocusPainted(false);
        filterButton.setToolTipText("Search for a specific topic in your saved topics");
        filterButton.addActionListener(e -> {
            String topic = topicInputField.getText().trim();
            if (!topic.isEmpty() && filterTopicController != null) {
                filterTopicController.execute(topic);
            } else if (topic.isEmpty()) {
                filterResultLabel.setText("Please enter a topic name.");
                filterResultLabel.setForeground(Color.RED);
            }
        });
        
        deleteButton = new JButton("Delete Topic");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteButton.setBackground(Color.BLACK);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setFocusPainted(false);
        deleteButton.setToolTipText("Remove a topic from your saved topics");
        deleteButton.addActionListener(e -> {
            String topic = topicInputField.getText().trim();
            if (!topic.isEmpty() && deleteTopicController != null) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the topic: " + topic + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteTopicController.execute(topic);
                    topicInputField.setText("");
                }
            } else if (topic.isEmpty()) {
                deleteResultLabel.setText("Please enter a topic name.");
                deleteResultLabel.setForeground(Color.RED);
            }
        });
        
        inputPanel.add(filterButton);
        inputPanel.add(deleteButton);
        
        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        filterResultLabel = new JLabel(" ");
        filterResultLabel.setForeground(new Color(0, 120, 0));
        deleteResultLabel = new JLabel(" ");
        deleteResultLabel.setForeground(new Color(200, 0, 0));
        resultPanel.add(filterResultLabel);
        resultPanel.add(deleteResultLabel);
        
        managePanel.add(inputPanel, BorderLayout.NORTH);
        managePanel.add(resultPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setFocusPainted(false);
        
        backButton.addActionListener(e -> {
            if (cardLayout != null && cardPanel != null) {
                cardLayout.show(cardPanel, "EnterTopicView");
            }
        });
        
        buttonPanel.add(backButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(savedTopicsPanel, BorderLayout.CENTER);
        centerPanel.add(managePanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshSavedTopics() {
        if (savedTopicRepository != null) {
            List<Topic> topics = savedTopicRepository.getAllTopics();
            String[] topicNames = topics.stream()
                    .map(Topic::getKeyword)
                    .filter(keyword -> keyword != null && !keyword.trim().isEmpty())
                    .toArray(String[]::new);
            savedTopicsList.setListData(topicNames);
        }
    }
    
    private void loadSavedTopic(String topicName) {
        if (savedTopicRepository != null && comparisonView != null && topicName != null) {
            List<Topic> topics = savedTopicRepository.getAllTopics();
            for (Topic topic : topics) {
                if (topic != null && topic.getKeyword() != null && 
                    topic.getKeyword().trim().equalsIgnoreCase(topicName.trim())) {
                    if (topic.getLeftSummary() != null && topic.getRightSummary() != null && 
                        topic.getComparisonSummary() != null) {
                        comparisonView.loadSavedTopic(topic);
                        return;
                    } else {
                        messageLabel.setText("Topic '" + topicName + "' exists but has no saved summaries.");
                        messageLabel.setForeground(Color.ORANGE);
                        return;
                    }
                }
            }
            messageLabel.setText("Topic '" + topicName + "' not found in saved topics.");
            messageLabel.setForeground(Color.RED);
        }
    }

    // configure view model listeners for delete and filter operations
    private void setupSavedTopicsPropertyChangeListeners() {
        if (deleteTopicViewModel != null) {
            deleteTopicViewModel.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName())) {
                    String errorMsg = deleteTopicViewModel.getState().getErrorMsg();
                    String topic = deleteTopicViewModel.getState().getTopic();
                    
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                        deleteResultLabel.setText(errorMsg);
                        deleteResultLabel.setForeground(Color.RED);
                    } else if (topic != null && !topic.isEmpty()) {
                        deleteResultLabel.setText(topic);
                        deleteResultLabel.setForeground(new Color(0, 150, 0));
                        topicInputField.setText("");
                        filterResultLabel.setText(" ");
                        refreshSavedTopics();
                    }
                }
            });
        }
        
        if (filterTopicViewModel != null) {
            filterTopicViewModel.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName())) {
                    String errorMsg = filterTopicViewModel.getState().getErrorMsg();
                    String topic = filterTopicViewModel.getState().getTopic();
                    
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                        filterResultLabel.setText(errorMsg);
                        filterResultLabel.setForeground(Color.RED);
                    } else if (topic != null && !topic.isEmpty()) {
                        filterResultLabel.setText(topic);
                        filterResultLabel.setForeground(new Color(0, 150, 0));
                    }
                }
            });
        }
    }
    
    public void setDeleteTopicController(DeleteSavedTopicController controller) {
        this.deleteTopicController = controller;
    }
    
    public void setFilterTopicController(FilterSavedTopicController controller) {
        this.filterTopicController = controller;
    }
    
    public void setDeleteTopicViewModel(DeleteSavedTopicViewModel viewModel) {
        this.deleteTopicViewModel = viewModel;
        setupSavedTopicsPropertyChangeListeners();
    }
    
    public void setFilterTopicViewModel(FilterSavedTopicViewModel viewModel) {
        this.filterTopicViewModel = viewModel;
        setupSavedTopicsPropertyChangeListeners();
    }

    public void setCardChange(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }
    
    public void setSavedTopicRepository(SavedTopicRepositoryImpl repository) {
        this.savedTopicRepository = repository;
        refreshSavedTopics();
    }
    
    public void setComparisonView(ComparisonView comparisonView) {
        this.comparisonView = comparisonView;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}

