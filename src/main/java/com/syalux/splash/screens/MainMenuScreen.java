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
        button.getStyleClass().add("menu-button"); // Uses the specific menu-button style
        button.setMaxWidth(Double.MAX_VALUE); // Let the VBox manage width
        button.setOnAction(e -> action.run());
        return button;
    }
}