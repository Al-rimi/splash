package com.syalux.splash.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Resource;
import com.syalux.splash.entities.FishEntity;
import javafx.beans.binding.Bindings;

public class DeathScreen extends StackPane {

    public DeathScreen(FishEntity killer, int score) {
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.09);");
        this.setAlignment(Pos.CENTER);

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("death-screen");

        // Title
        Text title = new Text(Resource.getString("game_over"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        title.setFill(Color.RED);

        // Killer Info
        Label killerLabel = new Label();
        if (killer != null) {
            String killerText = killer.isPlayer() ? 
                Resource.getString("killed_by_player") : 
                Resource.getString("killed_by_npc");
            killerLabel.setText(killerText);
        }
        killerLabel.setFont(Font.font("Arial", 24));
        killerLabel.setTextFill(Color.WHITE);

        // Score
        Label scoreLabel = new Label(Resource.getString("final_score") + ": " + score);
        scoreLabel.setFont(Font.font("Arial", 32));
        scoreLabel.setTextFill(Color.WHITE);

        // Funny Message
        String[] messages = {
            Resource.getString("death_message_1"),
            Resource.getString("death_message_2"),
            Resource.getString("death_message_3"),
            Resource.getString("death_message_4")
        };
        Label messageLabel = new Label(messages[(int) (Math.random() * messages.length)]);
        messageLabel.setFont(Font.font("Arial", 20));
        messageLabel.setTextFill(Color.LIGHTGRAY);

        // Buttons
        Button restartButton = createButton("new_game", this::handleRestart);
        Button menuButton = createButton("main_menu", Manager::showMainMenu);

        VBox buttonBox = new VBox(15, restartButton, menuButton);
        buttonBox.setAlignment(Pos.CENTER);

        container.getChildren().addAll(title, killerLabel, scoreLabel, messageLabel, buttonBox);
        this.getChildren().add(container);
    }

    private Button createButton(String resourceKey, Runnable action) {
        Button button = new Button();
        button.textProperty().bind(
            Bindings.createStringBinding(
                () -> Resource.getString(resourceKey),
                Resource.currentLocaleProperty()
            )
        );
        button.getStyleClass().add("death-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void handleRestart() {
        Manager.showGameScreen();
    }
}