package splash.ui.screens;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import splash.managers.GameManager;
import splash.core.entities.Player;

public class GameScreen {
    private final Pane root = new Pane();
    private final Player player;
    
    public GameScreen(Player player) {
        this.player = player;
    }
    
    public Scene createScene() {
        setupGameElements();
        setupInputHandling();
        return new Scene(root, 1280, 720);
    }
    
    private void setupInputHandling() {
        root.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case W, UP -> player.moveUp(true);
                case S, DOWN -> player.moveDown(true);
                case A, LEFT -> player.moveLeft(true);
                case D, RIGHT -> player.moveRight(true);
            }
        });
        
        root.setOnKeyReleased(e -> {
            switch(e.getCode()) {
                case W, UP -> player.moveUp(false);
                case S, DOWN -> player.moveDown(false);
                case A, LEFT -> player.moveLeft(false);
                case D, RIGHT -> player.moveRight(false);
            }
        });
    }
    
    private void setupGameElements() {
        // Initialize game elements
    }
}