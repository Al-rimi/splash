package com.syalux.splash.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Resource;
import com.syalux.splash.entities.FishEntity;
import javafx.beans.binding.Bindings;

public class DeathScreen extends StackPane {

    public DeathScreen(FishEntity killer, int score) {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("death-screen");

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Text title = new Text(Resource.getString("game_over"));
        title.getStyleClass().add("title");

        Label killerLabel = new Label();
        if (killer != null) {
            String killerText = killer.isPlayer() ?
                Resource.getString("killed_by_player") :
                Resource.getString("killed_by_npc");
            killerLabel.setText(killerText);
        }
        killerLabel.getStyleClass().add("killer-label");

        Label scoreLabel = new Label(Resource.getString("final_score") + ": " + score);
        scoreLabel.getStyleClass().add("score-label");

        String[] messages = {
            Resource.getString("death_message_1"),
            Resource.getString("death_message_2"),
            Resource.getString("death_message_3"),
            Resource.getString("death_message_4")
        };
        Label messageLabel = new Label(messages[(int) (Math.random() * messages.length)]);
        messageLabel.getStyleClass().add("message-label");

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