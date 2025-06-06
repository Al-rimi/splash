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

    /**
     * Initializes the game manager, loads configurations and profile,
     * and sets up the primary stage.
     * @param stage The primary stage of the application.
     */
    public static void init(Stage stage) {
        Config.loadConfig();
        loadProfile(); 
        Resource.loadAll();

        primaryStage = stage;
        primaryStage.setOnCloseRequest(event -> Manager.saveProfile());
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        
        // Initial window size based on loaded Config, or default if not loaded
        primaryStage.setWidth(Config.GAME_WIDTH * 0.8);
        primaryStage.setHeight(Config.GAME_HEIGHT * 0.8);
        
        primaryStage.setMaximized(true); // You might want this to be a profile setting
        primaryStage.setTitle(Resource.getString("title"));

        mainScene = new Scene(container);
        mainScene.getStylesheets().add(Resource.getStyleSheet());
        primaryStage.setScene(mainScene);
        primaryStage.show();

        showScreen(new WelcomeScreen());
    }

    /**
     * Displays a new screen with a fade-in effect, pushing the current screen onto the stack.
     * @param root The Parent node of the screen to display.
     */
    private static void showScreen(Parent root) {
        if (!container.getChildren().isEmpty()) {
            Parent current = (Parent) container.getChildren().get(0);
            screenStack.push(current);
            fadeOut(current, null); // Fade out the current screen
        }
        fadeIn(root); // Fade in the new screen
        container.getChildren().add(0, root);
    }

    /**
     * Applies a fade-in animation to a given Parent node.
     * @param root The Parent node to fade in.
     */
    private static void fadeIn(Parent root) {
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), root);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Applies a fade-out animation to a given Parent node and runs an optional action on completion.
     * @param root The Parent node to fade out.
     * @param postAction An optional Runnable to execute after the fade-out completes.
     */
    private static void fadeOut(Parent root, Runnable postAction) {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), root);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            container.getChildren().remove(root);
            if (postAction != null) postAction.run();
        });
        ft.play();
    }

    /**
     * Navigates back to the previous screen on the stack with a fade effect.
     */
    public static void goBack() {
        if (!screenStack.isEmpty()) {
            Parent current = (Parent) container.getChildren().get(0);
            Parent previous = screenStack.pop();
            fadeIn(previous);
            container.getChildren().add(0, previous);
            fadeOut(current, null);
        }
    }

    /**
     * Loads the user profile from a serialized file. Creates a new profile if none exists.
     */
    private static void loadProfile() {
        File file = new File(PROFILE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                profile = (Profile) ois.readObject();
                System.out.println("Profile loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading profile: " + e.getMessage());
                profile = new Profile(); // Create a new profile if loading fails
                System.out.println("Created a new default profile due to loading error.");
            }
        } else {
            profile = new Profile(); // Create a new profile if file doesn't exist
            System.out.println("No existing profile found. Created a new default profile.");
        }
    }

    /**
     * Saves the current user profile to a serialized file.
     */
    public static void saveProfile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFILE_FILE))) {
            oos.writeObject(profile);
            System.out.println("Profile saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets the current profile and immediately saves it.
     * @param newProfile The profile to set as current.
     */
    public static void setProfile(Profile newProfile) {
        Manager.profile = newProfile;
        saveProfile();
    }

    /**
     * Returns the currently loaded user profile.
     * @return The current Profile object.
     */
    public static Profile getProfile() {
        return profile;
    }

    /**
     * Clears the screen stack and navigates to the main menu.
     */
    public static void navigateToMainMenu() {
        screenStack.clear();
        showMainMenu();
    }

    // --- Screen Navigation Methods ---
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
        StackPane gameContainer = new StackPane(); // Use a local container for game and transition

        Parent gameRoot = new GameScreen(profile).createRoot();
        gameContainer.getChildren().add(gameRoot);

        BubbleTransitionScreen transition = new BubbleTransitionScreen(() -> {
            gameContainer.getChildren().remove(1); // Remove transition overlay after it finishes
        });
        gameContainer.getChildren().add(transition);

        showScreen(gameContainer);
    }

    /**
     * Returns the primary stage of the application.
     * @return The primary Stage.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}