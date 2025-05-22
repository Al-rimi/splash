package com.syalux.splash.application;

import com.syalux.splash.core.GameManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameManager.init(primaryStage);
        GameManager.showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}