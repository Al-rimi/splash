package splash.screens;

import java.util.stream.Stream;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import splash.core.GameEngine;
import splash.core.GameManager;
import splash.core.ResourceManager;
import splash.entities.*;

public class GameScreen {
    private final Player player;
    private Canvas gameCanvas;
    private final World world = new World();
    private final Timeline spawnTimer;
    private HBox hud;
    private final GameEngine gameEngine;
    private static final double SPAWN_RADIUS = 1000;
    private static final double DESPAWN_RADIUS = 4000;

    public GameScreen(Player player) {
        this.player = player;
        this.gameCanvas = new Canvas(GameManager.getGameWidth(), GameManager.getGameHeight());
        this.gameEngine = new GameEngine(player, world, gameCanvas);
        this.spawnTimer = new Timeline(new KeyFrame(Duration.seconds(3), e -> spawnEntities()));
        spawnTimer.setCycleCount(Animation.INDEFINITE);
        // setupWorld();
    }

    private void createHUD() {
        hud = new HBox(20);
        hud.getStyleClass().add("hud");
        hud.setPadding(new Insets(10));
        hud.setAlignment(Pos.TOP_LEFT);

        Label healthLabel = createDynamicLabel("health", player.healthProperty());
        Label levelLabel = createDynamicLabel("level", player.levelProperty());
        Label pointsLabel = createDynamicLabel("points", player.pointsProperty());
        Label coinsLabel = createDynamicLabel("coins", player.coinsProperty());

        Button stopButton = new Button();
        stopButton.textProperty().bind(
                Bindings.createStringBinding(() -> ResourceManager.getString("stop"),
                        ResourceManager.currentLocaleProperty()));
        stopButton.setOnAction(e -> {
            gameEngine.stop();
            GameManager.showMainMenu();
        });

        Stream.of(healthLabel, levelLabel, pointsLabel, coinsLabel)
                .forEach(label -> label.getStyleClass().add("hud-label"));

        stopButton.getStyleClass().add("nav-button");

        hud.getChildren().addAll(healthLabel, levelLabel,
                pointsLabel, coinsLabel, stopButton);
    }

    private Label createDynamicLabel(String key, IntegerProperty property) {
        Label label = new Label();
        label.textProperty().bind(
                Bindings.createStringBinding(() -> ResourceManager.getString(key) + ": " + property.get(),
                        property,
                        ResourceManager.currentLocaleProperty()));
        return label;
    }

    // private void setupWorld() {
    // }

    private void spawnEntities() {
        double dx = player.getVelocityX();
        double dy = player.getVelocityY();
        double length = Math.sqrt(dx * dx + dy * dy);
        double distance = 200 + Math.random() * SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;
        double dirX = (length == 0 ? Math.cos(angle) : dx / length);
        double dirY = (length == 0 ? Math.cos(angle) : dy / length);
        double x = player.getX() + dirX * distance;
        double y = player.getY() + dirY * distance;
        int fishType = (int)(Math.random() * 15) + 1;

        double spawnRate = Math.random();

        if (spawnRate < 0.3) {
            world.spawnEntity(new Enemy(player, x, y, ResourceManager.getEnemyImage(fishType, true), ResourceManager.getEnemyImage(fishType, false)));
        }
        if (Math.random() < 0.8) {
            world.spawnEntity(new Food(player, x, y, ResourceManager.getFoodImage(fishType, true), ResourceManager.getFoodImage(fishType, false)));
        }

        cleanupDistantEntities();
    }

    private void cleanupDistantEntities() {
        for (Fish entity : world.getEntities()) {
            if (entity instanceof Player) continue;

            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            if (dx * dx + dy * dy > DESPAWN_RADIUS * DESPAWN_RADIUS) {
                world.removeEntity(entity);
            }
        }
    }

    public Scene createScene() {
        StackPane rootContainer = new StackPane();
        rootContainer.getStyleClass().add("game-container");
        gameCanvas.getStyleClass().add("game-canvas");
        gameCanvas.widthProperty().bind(rootContainer.widthProperty());
        gameCanvas.heightProperty().bind(rootContainer.heightProperty());

        createHUD();
        rootContainer.getChildren().addAll(gameCanvas, hud);

        rootContainer.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        gameEngine.setRootContainer(rootContainer);
        rootContainer.requestFocus();

        gameEngine.start();
        spawnTimer.play();

        return new Scene(rootContainer);
    }
}