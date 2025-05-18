package splash.screens;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import splash.core.GameEngine;
import splash.core.GameManager;
import splash.core.ResourceManager;
import splash.core.Config;
import splash.entities.*;

import java.util.stream.Stream;

public class GameScreen {

    private final Player player;
    private final World world = new World();
    private final GameEngine gameEngine;
    private final Timeline spawnTimer;
    private final Image[] fishImagesLift = new Image[Config.FISH_IMAGE_COUNT];
    private final Image[] fishImagesRight = new Image[Config.FISH_IMAGE_COUNT];

    private Canvas gameCanvas;
    private HBox hud;

    public GameScreen(Player player) {
        this.player = player;
        this.gameCanvas = new Canvas(Config.GAME_WIDTH, Config.GAME_HEIGHT);
        this.gameEngine = new GameEngine(player, world, gameCanvas);
        this.spawnTimer = createSpawnTimer();
        for (int i = 0; i < Config.FISH_IMAGE_COUNT; i++) {
            fishImagesLift[i] = ResourceManager.getFishImage(i + 1, true);
            fishImagesRight[i] = ResourceManager.getFishImage(i + 1, false);
        }
    }

    private Timeline createSpawnTimer() {
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> spawnEntities()));
        timer.setCycleCount(Animation.INDEFINITE);
        return timer;
    }

    private void createHUD() {
        hud = new HBox(20);
        hud.setPadding(new Insets(10));
        hud.setAlignment(Pos.TOP_LEFT);
        hud.getStyleClass().add("hud");

        Label healthLabel = createBoundLabel("health", player.healthProperty());
        Label levelLabel = createBoundLabel("level", player.levelProperty());
        Label pointsLabel = createBoundLabel("points", player.pointsProperty());
        Label coinsLabel = createBoundLabel("coins", player.coinsProperty());

        Stream.of(healthLabel, levelLabel, pointsLabel, coinsLabel)
                .forEach(label -> label.getStyleClass().add("hud-label"));

        Button stopButton = createStopButton();

        hud.getChildren().addAll(healthLabel, levelLabel, pointsLabel, coinsLabel, stopButton);
    }

    private Label createBoundLabel(String key, IntegerProperty property) {
        Label label = new Label();
        label.textProperty().bind(
                Bindings.createStringBinding(
                        () -> ResourceManager.getString(key) + ": " + property.get(),
                        property,
                        ResourceManager.currentLocaleProperty()));
        return label;
    }

    private Button createStopButton() {
        Button stopButton = new Button();
        stopButton.textProperty().bind(
                Bindings.createStringBinding(
                        () -> ResourceManager.getString("stop"),
                        ResourceManager.currentLocaleProperty()));
        stopButton.setOnAction(e -> {
            gameEngine.stop();
            GameManager.showMainMenu();
        });
        stopButton.getStyleClass().add("nav-button");
        return stopButton;
    }

    private void spawnEntities() {
        double dx = player.getVelocityX();
        double dy = player.getVelocityY();
        double length = Math.hypot(dx, dy);
        double distance = 200 + Math.random() * Config.SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;

        double dirX = (length == 0) ? Math.cos(angle) : dx / length;
        double dirY = (length == 0) ? Math.sin(angle) : dy / length;
        double spawnX = player.getX() + dirX * distance;
        double spawnY = player.getY() + dirY * distance;

        int fishType = (int) (Math.random() * Config.FISH_IMAGE_COUNT) + 1;
        if (fishType == player.getCharacter()) {
            if (fishType >= Config.FISH_IMAGE_COUNT) {
                fishType -= 2;
            } else {
                fishType++;
            }
        }

        if (Math.random() < 0.3) {
            world.spawnEntity(Boat.createEnemy(
                    player,
                    spawnX, spawnY,
                    fishImagesLift[fishType],
                    fishImagesRight[fishType]));
        }

        if (Math.random() < 0.8) {
            world.spawnEntity(Boat.createFood(
                    player,
                    spawnX, spawnY,
                    fishImagesLift[fishType],
                    fishImagesRight[fishType]));
        }

        cleanupDistantEntities();
    }

    private void cleanupDistantEntities() {
        world.getEntities().removeIf(entity -> {
            if (entity instanceof Player)
                return false;
            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            return dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS;
        });
    }

    public Scene createScene() {
        StackPane root = new StackPane();
        root.getStyleClass().add("game-container");

        gameCanvas.getStyleClass().add("game-canvas");
        gameCanvas.widthProperty().bind(root.widthProperty());
        gameCanvas.heightProperty().bind(root.heightProperty());

        createHUD();
        root.getChildren().addAll(gameCanvas, hud);
        root.getStylesheets().add(ResourceManager.getStyleSheet());

        gameEngine.setRootContainer(root);
        root.requestFocus();
        gameEngine.start();
        spawnTimer.play();

        return new Scene(root);
    }
}
