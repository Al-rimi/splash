package com.syalux.splash.screens;

import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenuScreen {

    /**
     * Creates the root UI element for the main menu screen.
     * This method sets up the layout, adds style classes, and
     * creates buttons for starting the game, accessing the store,
     * settings, and exiting the application.
     *
     * @return The initialized Parent node representing the main menu.
     */
    public Parent createRoot() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("menu-container");
        layout.setPrefSize(Config.GAME_WIDTH, Config.GAME_HEIGHT);

        Button btnStart = createMenuButton("start_game", Manager::showGameScreen);
        Button btnStore = createMenuButton("store", Manager::showStoreScreen);
        Button btnSettings = createMenuButton("settings", Manager::showSettingsScreen);
        Button btnExit = createMenuButton("exit", () -> System.exit(0));

        layout.getChildren().addAll(btnStart, btnStore, btnSettings, btnExit);
        return layout;
    }

    private Button createMenuButton(String resourceKey, Runnable action) {
        Button button = new Button();
        button.textProperty().bind(Bindings.createStringBinding(
                () -> Resource.getString(resourceKey),
                Resource.currentLocaleProperty()
        ));
        button.getStyleClass().add("menu-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> action.run());
        return button;
    }
}