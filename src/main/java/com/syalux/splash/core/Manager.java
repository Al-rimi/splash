package com.syalux.splash.core;

import com.syalux.splash.data.Resource;
import com.syalux.splash.screens.GameScreen;
import com.syalux.splash.screens.MainMenuScreen;
import com.syalux.splash.screens.SettingsScreen;

import javafx.stage.Stage;

public final class Manager {
    private static Stage primaryStage;

    public static void init(Stage stage) {
        Resource.load();
        primaryStage = stage;
        primaryStage.setTitle(Resource.getString("title"));
        primaryStage.setMaximized(true);
    }

    public static void showMainMenu() {
        primaryStage.setScene(new MainMenuScreen().createScene());
        primaryStage.show();
    }

    public static void showSettingsScreen() {
        primaryStage.setScene(new SettingsScreen().createScene());
        primaryStage.show();
    }

    public static void showGameScreen() {
        primaryStage.setScene(new GameScreen().createScene());
        primaryStage.show();
    }
}
