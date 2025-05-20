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

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Splash");
        primaryStage.setMaximized(true);
    }

    public static void showMainMenu() {
        primaryStage.setScene(new MainMenuScreen().createScene());
        primaryStage.show();
    }

    public static void showSettingsScreen() {
        primaryStage.setScene(new SettingsScreen().createScene());
        primaryStage.show();
    }

    public static void startGame() {
        PlayerProfile profile = new PlayerProfile();
        int selectedCharacter = profile.getSelectedCharacter();

        Image image = ResourceManager.getFishImage(selectedCharacter);
        player = new Player(selectedCharacter, primaryStage.getWidth(), primaryStage.getHeight(), image);
        gameScreen = new GameScreen(player);

        primaryStage.setScene(gameScreen.createScene());
        primaryStage.show();
    }

    public static Player getPlayer() {
        return player;
    }
}
