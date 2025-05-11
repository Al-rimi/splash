package splash.screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import splash.utils.GameManager;
import splash.utils.ResourceManager;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;

public class MainMenuScreen {
    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("menu-container");
        
        // Create buttons with actions
        Button btnStart = this.createMenuButton("start_game", () -> GameManager.startGame());
        Button btnSettings = createMenuButton("settings", () -> GameManager.showSettingsScreen());
        Button btnExit = createMenuButton("exit", () -> System.exit(0));
        
        layout.getChildren().addAll(btnSettings, btnStart, btnExit);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        // Responsive font sizing
        layout.styleProperty().bind(Bindings.concat(
            "-fx-font-size: ", 
            scene.widthProperty().divide(50).asString(), 
            "px;"
        ));
        
        return scene;
    }

    private Button createMenuButton(String resourceKey, Runnable action) {
        Button btn = new Button();
        btn.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString(resourceKey),
                ResourceManager.currentLocaleProperty()
            )
        );
        btn.getStyleClass().add("menu-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> action.run());
        return btn;
    }
}