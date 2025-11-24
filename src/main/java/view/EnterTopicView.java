package view;

import interface_adapter.entertopic.EnterTopicController;
import interface_adapter.entertopic.EnterTopicState;
import interface_adapter.entertopic.EnterTopicViewModel;

import javax.swing.*;
import java.awt.*;

public class EnterTopicView extends JPanel {
    private static final String VIEW_NAME = "EnterTopicView";
    private final EnterTopicViewModel viewModel;

    private EnterTopicController controller;

    private final JTextArea searchArea = new JTextArea(15,40);
    private final JLabel errorLabel = new JLabel();
    private final JTextField topicTextField = new JTextField();
    private final JButton searchButton = new JButton("Search");

    public EnterTopicView(EnterTopicViewModel viewModel, EnterTopicController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        initializeUI();
        updateView();
    }


    public String getViewName() {
        return VIEW_NAME;
    }

    public void initializeUI (){
        setLayout(new BorderLayout());
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Search Topic"));
        searchArea.setEditable(false);
        searchArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        topicTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        errorLabel.setForeground(Color.RED);

        searchButton.addActionListener(e -> {
            controller.execute(topicTextField.getText());
            EnterTopicState state = viewModel.getState();
            String errorMsg = state.getErrorMessage();

            if (errorMsg != null && !errorMsg.isEmpty()) {
                errorLabel.setText(errorMsg);
                errorLabel.setVisible(true);
            } else{
                errorLabel.setText("");
                errorLabel.setVisible(false);
            }
        });
        mainPanel.add(new JLabel("Enter topic name:"), BorderLayout.NORTH);
        mainPanel.add(topicTextField);
        mainPanel.add(errorLabel);
        mainPanel.add(searchButton);

//        // Error panel
//        JPanel errorPanel = new JPanel(new BorderLayout());

//        errorPanel.add(errorLabel, BorderLayout.CENTER);

        add(mainPanel);
//        add(errorPanel, BorderLayout.SOUTH);
    }
    public void updateView() {
        if (viewModel.getState().getErrorMessage() != null && !viewModel.getState().getErrorMessage().isEmpty()) {
            errorLabel.setText(viewModel.getState().getErrorMessage());
        } else if (viewModel.getState().getTopic() != null && !viewModel.getState().getTopic().isEmpty()) {
            topicTextField.setText(viewModel.getState().getTopic());
        }
    }
}
