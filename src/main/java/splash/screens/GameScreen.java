package splash.screens;

import java.util.ArrayList;
import java.util.List;
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
import splash.engine.GameEngine;
import splash.entities.*;
import splash.utils.GameManager;
import splash.utils.ResourceManager;

public class GameScreen {
    private final Player player;
    private Canvas gameCanvas;
    private final World world = new World();
    private final Timeline spawnTimer;
    private HBox hud;
    private final GameEngine gameEngine;
    private static final double SPAWN_RADIUS = 500;
    private static final double DESPAWN_RADIUS = 1000;

    public GameScreen(Player player) {
        this.player = player;
        this.gameCanvas = new Canvas(1280, 720);
        this.gameEngine = new GameEngine(player, world, gameCanvas);
        this.spawnTimer = new Timeline(new KeyFrame(Duration.seconds(3), e -> spawnEntities()));
        spawnTimer.setCycleCount(Animation.INDEFINITE);
        setupWorld();
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

        stopButton.getStyleClass().add("nav-button"); // Add nav-button styling

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

    private void setupWorld() {
        world.spawnEntity(new Enemy(player, 200, 200));
    }

    private void spawnEntities() {
        // Spawn enemies
        if (Math.random() < 0.7) {
            spawnEntityAroundPlayer(SPAWN_RADIUS, true);
        }

        // Spawn food
        if (Math.random() < 0.5) {
            spawnEntityAroundPlayer(SPAWN_RADIUS, false);
        }

        // Despawn distant entities
        cleanupDistantEntities(DESPAWN_RADIUS);
    }

    private void spawnEntityAroundPlayer(double radius, boolean isEnemy) {
        double angle = Math.random() * 2 * Math.PI;
        double distance = Math.random() * radius;
        double x = player.getX() + Math.cos(angle) * distance;
        double y = player.getY() + Math.sin(angle) * distance;

        if (isEnemy) {
            world.spawnEntity(new Enemy(player, x, y));
        } else {
            world.spawnEntity(new Food(player, x, y, (int) (Math.random() * 10) + 5));
        }
    }

    private void cleanupDistantEntities(double despawnRadius) {
        List<Fish> toRemove = new ArrayList<>();
        double despawnSq = despawnRadius * despawnRadius;

        for (Fish entity : world.getEntities()) {
            if (entity instanceof Player)
                continue;

            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            if (dx * dx + dy * dy > despawnSq) {
                toRemove.add(entity);
            }
        }

        toRemove.forEach(world::removeEntity);
    }

    public Scene createScene() {
        StackPane rootContainer = new StackPane();
        rootContainer.getStyleClass().add("game-container"); // Add if you need additional styling
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