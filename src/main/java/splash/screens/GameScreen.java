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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import splash.engine.GameEngine;
import splash.entities.*;
import splash.managers.GameManager;
import splash.managers.ResourceManager;

public class GameScreen {
    private final Player player;
    private Canvas gameCanvas;
    private final World world = new World();
    private final Timeline spawnTimer;
    private HBox hud;
    private final double baseWidth = 1280;
    private final double baseHeight = 720;
    private final GameEngine gameEngine;

    public GameScreen(Player player) {
        this.player = player;
        this.gameCanvas = new Canvas();
        this.gameEngine = new GameEngine(player, world, gameCanvas);        
        this.spawnTimer = new Timeline(new KeyFrame(Duration.seconds(3), e -> spawnEntities()));        spawnTimer.setCycleCount(Animation.INDEFINITE);
        
        setupWorld();
    }

    private void createHUD() {
        hud = new HBox(20);
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

    private void setupInputHandling(StackPane rootContainer) {
        rootContainer.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        rootContainer.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                case UP:
                    player.moveUp(true);
                    break;
                case S:
                case DOWN:
                    player.moveDown(true);
                    break;
                case A:
                case LEFT:
                    player.moveLeft(true);
                    break;
                case D:
                case RIGHT:
                    player.moveRight(true);
                    break;
                default:
                    break;
            }
        });
    }

    private void setupWorld() {
        world.spawnEntity(new Enemy(player, 200, 200));
    }

    private void spawnEntities() {
        if (Math.random() < 0.7) {
            Enemy enemy = new Enemy(player, Math.random() * baseWidth * 2, Math.random() * baseHeight * 2);
            world.spawnEntity(enemy);
        }

        if (Math.random() < 0.5) {
            Food food = new Food(Math.random() * baseWidth * 2, Math.random() * baseHeight * 2, (int) (Math.random() * 10) + 5);
            world.spawnEntity(food);
        }
    }

    public Scene createScene() {
        StackPane rootContainer = new StackPane();
        gameCanvas.widthProperty().bind(rootContainer.widthProperty());
        gameCanvas.heightProperty().bind(rootContainer.heightProperty());

        createHUD();
        rootContainer.getChildren().addAll(gameCanvas, hud);

        rootContainer.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        rootContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a237e, #0d47a1);");

        setupInputHandling(rootContainer);
        rootContainer.requestFocus();

        gameEngine.start();
        spawnTimer.play();

        return new Scene(rootContainer);
    }
}