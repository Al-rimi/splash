package splash.ui.screens;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import splash.core.engine.GameLoop;
import splash.core.engine.RenderSystem;
import splash.core.entities.Player;

public class GameScreen {
    private final Pane root = new Pane();
    private final Player player;
    private final Canvas gameCanvas = new Canvas(1280, 720);
    private final RenderSystem renderSystem;
    private final GameLoop gameLoop;

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
        root.getChildren().add(gameCanvas);
        gameLoop.start();
        return new Scene(root, 1280, 720);
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