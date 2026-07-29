package com.helpdeskflow;

import com.helpdeskflow.view.MainFrame;
import javafx.application.Application;
import javafx.stage.Stage;

/** JavaFX application entry point. Bootstraps the main window via {@link MainFrame#showApplication}. */
public class HelpDeskFlowApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainFrame.showApplication(primaryStage);
    }
}
