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
        this.getStyleClass().add("pause-menu");
        this.setAlignment(Pos.CENTER);

        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);

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
        button.getStyleClass().add("pause-button");
        button.setOnAction(e -> action.run());
        return button;
    }
}