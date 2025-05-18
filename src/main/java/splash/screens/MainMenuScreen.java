package splash.screens;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import splash.core.Config;
import splash.core.GameManager;
import splash.core.ResourceManager;

public class MainMenuScreen {

    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("menu-container");
        layout.setPrefSize(Config.GAME_WIDTH, Config.GAME_HEIGHT);

        Button btnStart = createMenuButton("start_game", GameManager::startGame);
        Button btnSettings = createMenuButton("settings", GameManager::showSettingsScreen);
        Button btnExit = createMenuButton("exit", () -> System.exit(0));

        layout.getChildren().addAll(btnSettings, btnStart, btnExit);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(ResourceManager.getStyleSheet());
        return scene;
    }

    private Button createMenuButton(String resourceKey, Runnable action) {
        Button button = new Button();
        button.textProperty().bind(Bindings.createStringBinding(
            () -> ResourceManager.getString(resourceKey),
            ResourceManager.currentLocaleProperty()
        ));
        button.getStyleClass().add("menu-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> action.run());
        return button;
    }
}