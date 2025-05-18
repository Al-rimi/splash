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
    }

    public static void startGame() {
        PlayerProfile profile = new PlayerProfile();
        int selectedCharacter = profile.getSelectedCharacter();

        Image leftImage = ResourceManager.getFishImage(selectedCharacter, true);
        Image rightImage = ResourceManager.getFishImage(selectedCharacter, false);

        if (leftImage == null || rightImage == null) {
            throw new RuntimeException("Could not load character images");
        }

        player = new Player(leftImage, rightImage, primaryStage.getWidth(), primaryStage.getHeight());
        gameScreen = new GameScreen(player);

        primaryStage.setScene(gameScreen.createScene());
    }

    public static Player getPlayer() {
        return player;
    }
}
