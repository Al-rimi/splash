package com.syalux.splash.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Resource;

import javafx.beans.binding.Bindings;

public class PauseScreen extends StackPane {

    public PauseScreen(Runnable onResume) {
        // Apply the 'pause-menu' style to the StackPane itself, which includes background color
        this.getStyleClass().add("pause-menu");
        // Removed setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); as it's now in CSS
        this.setAlignment(Pos.CENTER);

        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        // No need for menu.getStyleClass().add("pause-menu"); if the StackPane handles the background
        // If you need a specific inner styling for the VBox, give it a new class like "pause-menu-content"

        Button resumeButton = createPauseButton("resume", onResume);
        Button settingsButton = createPauseButton("settings", Manager::showSettingsScreen);
        Button exitButton = createPauseButton("exit_to_menu", Manager::showMainMenu);

        menu.getChildren().addAll(resumeButton, settingsButton, exitButton);
        this.getChildren().add(menu);
    }

    private Button createPauseButton(String resourceKey, Runnable action) {
        Button button = new Button();
        button.textProperty().bind(
            Bindings.createStringBinding(
                () -> Resource.getString(resourceKey),
                Resource.currentLocaleProperty()
            )
        );
        button.getStyleClass().add("pause-button"); // Uses the specific pause-button style
        button.setOnAction(e -> action.run());
        return button;
    }
}