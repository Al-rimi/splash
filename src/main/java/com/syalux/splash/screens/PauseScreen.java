package com.syalux.splash.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.syalux.splash.core.ResourceManager;

import javafx.beans.binding.Bindings;

public class PauseScreen extends StackPane {

    public PauseScreen(Runnable onResume, Runnable onSettings, Runnable onExit) {
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        this.setAlignment(Pos.CENTER);

        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.getStyleClass().add("pause-menu");

        Button resumeButton = createPauseButton("resume", onResume);
        Button settingsButton = createPauseButton("settings", onSettings);
        Button exitButton = createPauseButton("exit_to_menu", onExit);

        menu.getChildren().addAll(resumeButton, settingsButton, exitButton);
        this.getChildren().add(menu);
    }

    private Button createPauseButton(String resourceKey, Runnable action) {
        Button button = new Button();
        button.textProperty().bind(
            Bindings.createStringBinding(
                () -> ResourceManager.getString(resourceKey),
                ResourceManager.currentLocaleProperty()
            )
        );
        button.getStyleClass().add("pause-button");
        button.setOnAction(e -> action.run());
        return button;
    }
}