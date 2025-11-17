package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        try {
            // Ensure GUI runs on EDT
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        AppBuilder appBuilder = new AppBuilder();

                        JFrame application = appBuilder
                            .addComparisonView()
                            .addComparisonUseCase()
                            .build();

                        // Get the card panel and layout for navigation
                        // The cardPanel is the first (and only) component in CENTER position
                        JPanel cardPanel = null;
                        for (Component comp : application.getContentPane().getComponents()) {
                            if (comp instanceof JPanel) {
                                cardPanel = (JPanel) comp;
                                break;
                            }
                        }
                        CardLayout cardLayout = cardPanel != null ? (CardLayout) cardPanel.getLayout() : null;
                        
                        // No navigation buttons - just show comparison view
                        // The left summary can still be accessed via the "Use in Comparison" flow

                        application.setMinimumSize(new Dimension(800, 600));
                        application.setSize(1000, 700);
                        application.setLocationRelativeTo(null);
                        application.setAlwaysOnTop(true); // Bring to front initially
                        application.setVisible(true);
                        application.setState(Frame.NORMAL); // Ensure not minimized
                        application.toFront();
                        application.requestFocus();
                        application.repaint();
                        
                        // Force window to front multiple times
                        for (int i = 0; i < 3; i++) {
                            SwingUtilities.invokeLater(() -> {
                                application.toFront();
                                application.requestFocus();
                                application.repaint();
                            });
                        }
                        
                        // Remove always on top after a moment
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                SwingUtilities.invokeLater(() -> {
                                    application.setAlwaysOnTop(false);
                                });
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }).start();
                        
                        System.out.println("Application window should be visible now.");
                        System.out.println("Window size: " + application.getSize());
                        System.out.println("Window location: " + application.getLocation());
                        System.out.println("Window visible: " + application.isVisible());
                        System.out.println("Window showing: " + application.isShowing());
                    } catch (Exception e) {
                        System.err.println("Error creating application: " + e.getMessage());
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, 
                            "Error starting application: " + e.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
