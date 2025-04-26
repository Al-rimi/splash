
package splash.ui.screens;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import splash.managers.GameManager;

public class MainMenuScreen {
    public Scene createScene() {
        VBox layout = new VBox(20);
        Button btnStart = new Button("Start Game");
        Button btnExit = new Button("Exit");
        
        btnStart.setOnAction(e -> GameManager.startGame());
        btnExit.setOnAction(e -> System.exit(0));
        
        return new Scene(layout, 800, 600);
    }
}