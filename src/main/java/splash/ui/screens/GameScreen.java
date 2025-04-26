package splash.ui.screens;

import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import splash.core.engine.GameLoop;
import splash.core.engine.RenderSystem;
import splash.core.entities.Player;
import splash.managers.GameManager;
import splash.managers.ResourceManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;

public class GameScreen {
    private final Pane root = new Pane();
    private final Player player;
    private final Canvas gameCanvas = new Canvas(1280, 720);
    private final RenderSystem renderSystem;
    private final GameLoop gameLoop;
    private HBox hud;

    public GameScreen(Player player) {
        this.player = player;
        this.renderSystem = new RenderSystem(gameCanvas);
        this.gameLoop = new GameLoop() {
            @Override
            protected void update(double deltaTime) {
                updateGame(deltaTime);
                renderGame();
            }
        };
    }

    private void updateGame(double deltaTime) {
        player.update(deltaTime);
    }

    private void renderGame() {
        renderSystem.clear();
        renderSystem.render(player);
    }

    public Scene createScene() {
        setupGameElements();
        setupInputHandling();
        createHUD();
        
        StackPane rootContainer = new StackPane();
        rootContainer.getChildren().addAll(gameCanvas, hud);
        
        gameLoop.start();
        return new Scene(rootContainer, 1280, 720);
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
            Bindings.createStringBinding(() -> 
                ResourceManager.getString("stop"),
                ResourceManager.currentLocaleProperty()
            )
        );
        stopButton.setOnAction(e -> {
            gameLoop.stop();
            GameManager.showMainMenu();
        });

        hud.getChildren().addAll(healthLabel, levelLabel, 
                              pointsLabel, coinsLabel, stopButton);
    }

    private Label createDynamicLabel(String key, IntegerProperty property) {
        Label label = new Label();
        label.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString(key) + ": " + property.get(),
                property,
                ResourceManager.currentLocaleProperty()
            )
        );
        return label;
    }

    private void setupInputHandling() {
        root.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case W, UP -> player.moveUp(true);
                case S, DOWN -> player.moveDown(true);
                case A, LEFT -> player.moveLeft(true);
                case D, RIGHT -> player.moveRight(true);
                default -> throw new IllegalArgumentException("Unexpected value: " + e.getCode());
            }
        });
        
        root.setOnKeyReleased(e -> {
            switch(e.getCode()) {
                case W, UP -> player.moveUp(false);
                case S, DOWN -> player.moveDown(false);
                case A, LEFT -> player.moveLeft(false);
                case D, RIGHT -> player.moveRight(false);
                default -> throw new IllegalArgumentException("Unexpected value: " + e.getCode());
            }
        });
    }
    
    private void setupGameElements() {
        // Initialize game elements
    }
}