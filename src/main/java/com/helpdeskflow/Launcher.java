package com.helpdeskflow;

import javafx.application.Application;

/** Separate entry point that avoids JavaFX runtime resolution at class-load time.
 *  Delegates to {@link HelpDeskFlowApplication}. */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(HelpDeskFlowApplication.class, args);
    }
}
