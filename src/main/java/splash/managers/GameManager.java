package splash.managers;

import javafx.stage.Stage;
import splash.ui.screens.*;
import splash.core.entities.Player;

import java.io.InputStream;

import javafx.scene.image.Image;

public final class GameManager {
    private static Stage primaryStage;
    private static GameScreen gameScreen;
    private static Player player;
    
    private GameManager() {}
    
    public static void initialize(Stage stage) {
        primaryStage = stage;
        
        InputStream imageStream = GameManager.class.getResourceAsStream("/images/character.png");
        if(imageStream != null) {
            player = new Player(new Image(imageStream));
        } else {
            throw new RuntimeException("Could not load character image");
        }
        
        gameScreen = new GameScreen(player);
    }
    
    public static void showMainMenu() {
        primaryStage.setScene(new MainMenuScreen().createScene());
        primaryStage.show();
    }
    
    public static void startGame() {
        primaryStage.setScene(gameScreen.createScene());
    }
    
    public static Player getPlayer() {
        return player;
    }
}