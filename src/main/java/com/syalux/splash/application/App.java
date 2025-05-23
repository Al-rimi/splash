package com.syalux.splash.application;

import com.syalux.splash.core.Manager;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Manager.init(primaryStage);
        Manager.showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}