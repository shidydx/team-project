package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();

        JFrame application = appBuilder.addEnterTopicView().addEnterTopicUseCase()
                .addLeftNewsSummaryView().addLeftNewsSummaryUseCase()
                .addSearchHistoryView().addSearchHistoryUseCase()
                .addRightNewsSummaryView().addRightNewsSummaryUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
