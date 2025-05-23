package com.syalux.splash.core;

import com.syalux.splash.data.Profile;
import com.syalux.splash.data.Resource;
import com.syalux.splash.entities.Player;
import com.syalux.splash.screens.GameScreen;
import com.syalux.splash.screens.MainMenuScreen;
import com.syalux.splash.screens.SettingsScreen;

import javafx.stage.Stage;

public final class Manager {
    private static Stage primaryStage;
    private static GameScreen gameScreen;
    private static Player player;

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

    public static void startGame() {
        Profile profile = new Profile();
        int selectedCharacter = profile.getSelectedCharacter();

        player = new Player(primaryStage.getWidth(), primaryStage.getHeight(), selectedCharacter);
        gameScreen = new GameScreen(player);
        primaryStage.setScene(gameScreen.createScene());
        primaryStage.show();
    }

    public static Player getPlayer() {
        return player;
    }
}
