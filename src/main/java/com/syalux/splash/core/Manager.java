package com.syalux.splash.core;

import com.syalux.splash.data.Resource;
import com.syalux.splash.data.Config;
import com.syalux.splash.screens.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import java.util.ArrayDeque;
import java.util.Deque;

public final class Manager {
    private static Stage primaryStage;
    private static Scene mainScene;
    private static final Deque<Parent> screenStack = new ArrayDeque<>();

    public static void init(Stage stage) {
        primaryStage = stage;
        mainScene = new Scene(new StackPane());

        Resource.loadLanguage(Config.DEFAULT_LANGUAGE);
        Resource.loadStyles();

        primaryStage.setMaximized(true);
        primaryStage.setTitle(Resource.getString("title"));
        mainScene.getStylesheets().add(Resource.getStyleSheet());
        primaryStage.setScene(mainScene);

        showScreen(new WelcomeScreen());
        primaryStage.show();

        Resource.loadImages();
    
        showMainMenu();
    }

    private static void showScreen(Parent root) {
        if (mainScene.getRoot() != null) {
            screenStack.push(mainScene.getRoot());
        }
        mainScene.setRoot(root);
    }

    public static void goBack() {
        if (!screenStack.isEmpty()) {
            mainScene.setRoot(screenStack.pop());
        }
    }

    public static void navigateToMainMenu() {
        screenStack.clear();
        showMainMenu();
    }

    public static void showMainMenu() {
        showScreen(new MainMenuScreen().createRoot());
    }

    public static void showSettingsScreen() {
        showScreen(new SettingsScreen().createRoot());
    }

    public static void showGameScreen() {
        StackPane container = new StackPane();
        
        Parent gameRoot = new GameScreen().createRoot();
        container.getChildren().add(gameRoot);
        
        // Add bubble transition overlay
        BubbleTransitionScreen transition = new BubbleTransitionScreen(() -> {
            container.getChildren().remove(1);
        });
        container.getChildren().add(transition);
        
        showScreen(container);
    }
}