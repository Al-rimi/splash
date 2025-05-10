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
import splash.engine.CollisionSystem;
import splash.engine.GameLoop;
import splash.engine.RenderSystem;
import splash.entities.*;
import splash.managers.GameManager;
import splash.managers.ResourceManager;

public class GameScreen {
    private final Player player;
    private final Canvas gameCanvas = new Canvas(1280, 720);
    private final RenderSystem renderSystem;
    private final GameLoop gameLoop;
    private final DynamicWorld world = new DynamicWorld();
    private final CollisionSystem collisionSystem;
    private final Timeline spawnTimer;
    private HBox hud;

    public GameScreen(Player player) {
        this.player = player;
        this.renderSystem = new RenderSystem(gameCanvas);
        this.collisionSystem = new CollisionSystem(player, world);
        this.gameLoop = new GameLoop() {
            @Override
            protected void update(double deltaTime) {
                updateGame(deltaTime);
                renderGame();
            }
        };
        spawnTimer = new Timeline(new KeyFrame(Duration.seconds(3), e -> spawnEntities()));
        spawnTimer.setCycleCount(Animation.INDEFINITE);
        
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

    private void setupInputHandling(StackPane rootContainer) {
        rootContainer.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        
        rootContainer.setOnKeyPressed(e -> {
            System.out.println("Key PRESSED: " + e.getCode());
            switch(e.getCode()) {
                case W:
                case UP:
                    System.out.println("Moving UP");
                    player.moveUp(true);
                    break;
                case S:
                case DOWN:
                    System.out.println("Moving DOWN");
                    player.moveDown(true);
                    break;
                case A:
                case LEFT:
                    System.out.println("Moving LEFT");
                    player.moveLeft(true);
                    break;
                case D:
                case RIGHT:
                    System.out.println("Moving RIGHT");
                    player.moveRight(true);
                    break;
                default:
                    System.out.println("Unhandled key: " + e.getCode());
                    break;
            }
        });
        
        rootContainer.setOnKeyReleased(e -> {
            System.out.println("Key RELEASED: " + e.getCode());
            switch(e.getCode()) {
                case W:
                case UP:
                    player.moveUp(false);
                    break;
                case S:
                case DOWN:
                    player.moveDown(false);
                    break;
                case A:
                case LEFT:
                    player.moveLeft(false);
                    break;
                case D:
                case RIGHT:
                    player.moveRight(false);
                    break;
                default:
                    System.out.println("Unhandled key: " + e.getCode());
                    break;
            }
        });
    }

    private void setupWorld() {
        // Debug initial player position
        System.out.println("Initial player position: " + player.getX() + ", " + player.getY());
        
        // Initial enemies
        world.spawnEntity(new Enemy(player, 200, 200));
    }

    private void spawnEntities() {
        // Debug spawns
        System.out.println("Spawning new entities...");
        
        if(Math.random() < 0.7) {
            Enemy enemy = new Enemy(player, Math.random() * 1280, Math.random() * 720);
            System.out.println("Spawned enemy at: " + enemy.getX() + ", " + enemy.getY());
            world.spawnEntity(enemy);
        }
        
        if(Math.random() < 0.5) {
            Food food = new Food(Math.random() * 1280, Math.random() * 720, (int)(Math.random() * 10) + 5);
            System.out.println("Spawned food at: " + food.getX() + ", " + food.getY());
            world.spawnEntity(food);
        }
    }

    private void updateGame(double deltaTime) {    
        player.update(deltaTime);
        world.getEntities().forEach(e -> e.update(deltaTime));
        collisionSystem.checkCollisions();
    }

    private void renderGame() {
        renderSystem.clear();
        world.getEntities().forEach(renderSystem::render);
        renderSystem.render(player);
    }

    public Scene createScene() {
        StackPane rootContainer = new StackPane();
        rootContainer.getChildren().addAll(gameCanvas);
        createHUD();
        rootContainer.getChildren().add(hud);
        
        // Setup input on the actual root container
        setupInputHandling(rootContainer);
        
        // Ensure focus for key events
        rootContainer.setFocusTraversable(true);
        rootContainer.requestFocus();
        
        gameLoop.start();
        spawnTimer.play();
        
        return new Scene(rootContainer, 1280, 720);
    }
}