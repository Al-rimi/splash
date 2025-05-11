package splash.core;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import splash.data.PlayerProfile;
import splash.entities.Player;
import splash.screens.*;

public final class GameManager {
    private static Stage primaryStage;
    private static GameScreen gameScreen;
    private static Player player;
    private static final double GAME_WIDTH = 1280;
    private static final double GAME_HEIGHT = 720;
    
    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Splash");
        primaryStage.setMaximized(true);
        
        showMainMenu();
    }
    
    public static void showMainMenu() {
        primaryStage.setScene(new MainMenuScreen().createScene());
        primaryStage.show();
    }

    public static void showSettingsScreen() {
        primaryStage.setScene(new SettingsScreen().createScene());
    }
    
    public static void startGame() {
        PlayerProfile profile = new PlayerProfile();
        String selectedCharacter = profile.getSelectedCharacter() != null ? 
            profile.getSelectedCharacter() : "player-1";
        
        Image leftImage = ResourceManager.getPlayerImage(selectedCharacter, true);
        Image rightImage = ResourceManager.getPlayerImage(selectedCharacter, false);
        
        if(leftImage != null && rightImage != null) {
            player = new Player(leftImage, rightImage, 
                primaryStage.getWidth(), primaryStage.getHeight());
        } else {
            throw new RuntimeException("Could not load character images");
        }
        
        gameScreen = new GameScreen(player);

        primaryStage.setScene(gameScreen.createScene());
    }
    
    public static Player getPlayer() {
        return player;
    }

    public static int getGameWidth() {
        return (int) GAME_WIDTH;
    }

    public static int getGameHeight() {
        return (int) GAME_HEIGHT;
    }
}