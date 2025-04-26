package splash.ui.screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import splash.managers.GameManager;
import splash.managers.ResourceManager;
import javafx.beans.binding.Bindings;

public class MainMenuScreen {
    public Scene createScene() {
        VBox layout = new VBox(20);
        Button btnStart = new Button(ResourceManager.getString("start_game"));
        Button btnExit = new Button(ResourceManager.getString("exit"));
        Button btnSettings = new Button();

        btnSettings.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString("settings"),
                ResourceManager.currentLocaleProperty()
            )
        );
        btnSettings.setOnAction(e -> GameManager.showSettingsScreen());

        btnStart.setOnAction(e -> GameManager.startGame());
        btnExit.setOnAction(e -> System.exit(0));

        layout.getChildren().addAll(btnSettings, btnStart, btnExit);
        return new Scene(layout, 800, 600);
    }
}