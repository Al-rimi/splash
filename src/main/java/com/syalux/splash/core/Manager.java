package com.syalux.splash.core;

import com.syalux.splash.data.Resource;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Profile;
import com.syalux.splash.screens.*;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import javafx.util.Duration;

public final class Manager {
    private static Stage primaryStage;
    private static Scene mainScene;
    private static final StackPane container = new StackPane();
    private static final Deque<Parent> screenStack = new ArrayDeque<>();
    private static final String PROFILE_FILE = "user_profile.ser";
    private static Profile profile;

    public static void init(Stage stage) {
        Config.loadConfig();
        loadProfile();
        Resource.loadAll();

        primaryStage = stage;
        primaryStage.setOnCloseRequest(event -> Manager.saveProfile());
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setWidth(Config.GAME_WIDTH * 0.8);
        primaryStage.setHeight(Config.GAME_HEIGHT * 0.8);

        primaryStage.setMaximized(true);
        primaryStage.setTitle(Resource.getString("title"));

        mainScene = new Scene(container);
        mainScene.getStylesheets().add(Resource.getStyleSheet());
        primaryStage.setScene(mainScene);
        primaryStage.show();

        showScreen(new WelcomeScreen());
    }

    private static void showScreen(Parent root) {
        if (!container.getChildren().isEmpty()) {
            Parent current = (Parent) container.getChildren().get(0);
            screenStack.push(current);
            fadeOut(current, null);
        }
        fadeIn(root);
        container.getChildren().add(0, root);
    }

    private static void fadeIn(Parent root) {
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), root);
        ft.setToValue(1);
        ft.play();
    }

    private static void fadeOut(Parent root, Runnable postAction) {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), root);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            container.getChildren().remove(root);
            if (postAction != null) postAction.run();
        });
        ft.play();
    }

    public static void goBack() {
        if (!screenStack.isEmpty()) {
            Parent current = (Parent) container.getChildren().get(0);
            Parent previous = screenStack.pop();
            fadeIn(previous);
            container.getChildren().add(0, previous);
            fadeOut(current, null);
        }
    }

    private static void loadProfile() {
        File file = new File(PROFILE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                profile = (Profile) ois.readObject();
                System.out.println("Profile loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading profile: " + e.getMessage());
                profile = new Profile();
                System.out.println("Created a new default profile due to loading error.");
            }
        } else {
            profile = new Profile();
            System.out.println("No existing profile found. Created a new default profile.");
        }
    }

    public static void saveProfile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFILE_FILE))) {
            oos.writeObject(profile);
            System.out.println("Profile saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setProfile(Profile newProfile) {
        Manager.profile = newProfile;
        saveProfile();
    }

    public static Profile getProfile() {
        return profile;
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

    public static void showStoreScreen() {
        showScreen(new StoreScreen().createRoot());
    }

    public static void showGameScreen() {
        StackPane gameContainer = new StackPane();

        Parent gameRoot = new GameScreen(profile).createRoot();
        gameContainer.getChildren().add(gameRoot);

        BubbleTransitionScreen transition = new BubbleTransitionScreen(() -> {
            gameContainer.getChildren().remove(1);
        });
        gameContainer.getChildren().add(transition);

        showScreen(gameContainer);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}