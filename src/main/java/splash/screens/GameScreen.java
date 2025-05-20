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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import splash.core.GameEngine;
import splash.core.GameManager;
import splash.core.ResourceManager;
import splash.core.Config;
import splash.entities.Player;
import splash.entities.World;
import splash.entities.StaticEntity;
import splash.entities.NPC;

import java.util.Random;
import java.util.stream.Stream;

public class GameScreen {

    private final Player player;
    private final World world = new World();
    private final GameEngine gameEngine;
    private final Timeline spawnTimer;
    private final Image[] fishImages = new Image[Config.FISH_IMAGE_COUNT];
    private final Image[] mountains = new Image[Config.MOUNTAIN_IMAGE_COUNT];
    private final Image[] seaweeds = new Image[Config.SEAWEED_IMAGE_COUNT];
    private final Image[] rocks = new Image[Config.ROCK_IMAGE_COUNT];
    private final Random random = new Random();
    private StackPane root;
    private boolean isPaused = false;

    private Canvas gameCanvas;
    private Canvas backgroundCanvas;
    private HBox hud;

    public GameScreen(Player player) {
        this.player = player;
        this.gameCanvas = new Canvas(Config.GAME_WIDTH, Config.GAME_HEIGHT);
        this.gameEngine = new GameEngine(player, world, gameCanvas, this::togglePause);
        this.spawnTimer = createSpawnTimer();
        for (int i = 0; i < Config.FISH_IMAGE_COUNT; i++) {
            fishImages[i] = ResourceManager.getFishImage(i + 1);
        }
        for (int i = 0; i < Config.MOUNTAIN_IMAGE_COUNT; i++) {
            mountains[i] = ResourceManager.getMountainImage(i + 1);
        }
        for (int i = 0; i < Config.SEAWEED_IMAGE_COUNT; i++) {
            seaweeds[i] = ResourceManager.getSeaweedImage(i + 1);
        }
        for (int i = 0; i < Config.ROCK_IMAGE_COUNT; i++) {
            rocks[i] = ResourceManager.getRockImage(i + 1);
        }

        this.world.spawnEntity(player);
    }

    private Timeline createSpawnTimer() {
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(Config.SPAWN_DURATION_SECONDS), e -> spawnEntities()));
        timer.setCycleCount(Animation.INDEFINITE);
        return timer;
    }

    private void createHUD() {
        hud = new HBox(20);
        hud.setPadding(new Insets(10));
        hud.setAlignment(Pos.TOP_LEFT);
        hud.getStyleClass().add("hud");

        Label healthLabel = createBoundLabel("health", player.healthProperty());
        Label scoreLabel = createBoundLabel("score", player.scoreProperty());
        Label coinsLabel = createBoundLabel("coins", player.coinsProperty());

        Stream.of(healthLabel, scoreLabel, coinsLabel)
                .forEach(label -> label.getStyleClass().add("hud-label"));

        Button pauseButton = createPauseButton();
        hud.getChildren().addAll(healthLabel, scoreLabel, coinsLabel, pauseButton);
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

    private Button createPauseButton() {
        Button pauseButton = new Button();
        pauseButton.textProperty().bind(
                Bindings.createStringBinding(
                        () -> ResourceManager.getString("pause"),
                        ResourceManager.currentLocaleProperty()));
        pauseButton.setOnAction(e -> togglePause());
        pauseButton.getStyleClass().add("nav-button");
        return pauseButton;
    }

    private void togglePause() {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        isPaused = true;
        gameEngine.setPaused(isPaused);
        spawnTimer.pause();
        applyBlurEffect(isPaused);
        showPauseScreen();
    }

    private void resumeGame() {
        isPaused = false;
        gameEngine.setPaused(isPaused);
        spawnTimer.play();
        applyBlurEffect(isPaused);
        hidePauseScreen();
    }

    private void applyBlurEffect(boolean apply) {
        GaussianBlur blur = new GaussianBlur(10);
        if (apply) {
            gameCanvas.setEffect(blur);
            backgroundCanvas.setEffect(blur);
        } else {
            gameCanvas.setEffect(null);
            backgroundCanvas.setEffect(null);
        }
    }

    private void showPauseScreen() {
        PauseScreen pauseScreen = new PauseScreen(
                this::resumeGame,
                () -> GameManager.showSettingsScreen(),
                () -> {
                    gameEngine.stop();
                    spawnTimer.stop();
                    GameManager.showMainMenu();
                });
        root.getChildren().add(pauseScreen);
    }

    private void hidePauseScreen() {
        root.getChildren().removeIf(node -> node instanceof PauseScreen);
    }

    private void spawnEntities() {
        double dx = player.getVelocityX();
        double dy = player.getVelocityY();
        double length = Math.hypot(dx, dy);
        double distance = 1500 + Math.random() * Config.SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;

        double dirX = (length == 0) ? Math.cos(angle) : dx / length;
        double dirY = (length == 0) ? Math.sin(angle) : dy / length;
        double spawnX = player.getX() + dirX * distance;
        double spawnY = player.getY() + dirY * distance;

        int fishType = (int) (random.nextDouble() * Config.FISH_IMAGE_COUNT);
        if (fishType == player.getCharacter()) {
            if (fishType >= Config.FISH_IMAGE_COUNT) {
                fishType -= 2;
            } else {
                fishType++;
            }
        }

        if (random.nextDouble() < Config.SPAWN_ENEMY_PROBABILITY) {
            NPC enemy = new NPC(world, spawnX, spawnY,
                    fishImages[fishType], random.nextDouble() * player.getSize() * 1.5 + player.getSize());
            world.spawnEntity(enemy);
        }

        if (random.nextDouble() < Config.SPAWN_FOOD_PROBABILITY) {
            NPC food = new NPC(world, spawnX, spawnY,
                    fishImages[fishType], random.nextDouble() * player.getSize() * 0.8 + player.getSize() * 0.2);
            world.spawnEntity(food);
        }

        if (random.nextDouble() < Config.SPAWN_MOUNTAIN_PROBABILITY) {
            Image mountain = mountains[random.nextInt(Config.MOUNTAIN_IMAGE_COUNT)];
            world.spawnStaticEntity(new StaticEntity(
                    spawnX * 10, spawnY * 10,
                    mountain,
                    random.nextDouble() * 2 + 1.2,
                    random.nextDouble() * 0.2 + 0.1));
        }

        if (random.nextDouble() < Config.SPAWN_ROCK_PROBABILITY) {
            Image rock = rocks[random.nextInt(Config.ROCK_IMAGE_COUNT)];
            world.spawnStaticEntity(new StaticEntity(
                    spawnX + random.nextDouble() * 200 - 100,
                    spawnY + random.nextDouble() * 200 - 100,
                    rock,
                    random.nextDouble() * 0.3 + 0.1,
                    random.nextDouble() * 0.6 + 0.3));
        }

        if (random.nextDouble() < Config.SPAWN_SEAWEED_PROBABILITY) {
            Image seaweed = seaweeds[random.nextInt(Config.SEAWEED_IMAGE_COUNT)];
            world.spawnStaticEntity(new StaticEntity(
                    spawnX + random.nextDouble() * 300 - 150,
                    spawnY + random.nextDouble() * 300 - 150,
                    seaweed,
                    random.nextDouble() * 0.4 + 0.2,
                    random.nextDouble() * 0.8 + 0.2));
        }

        cleanupDistantEntities();
    }

    private void cleanupDistantEntities() {
        world.getEntities().removeIf(entity -> {
            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            return (dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS) || entity.isDead();
        });

        world.getStaticEntities().removeIf(entity -> {
            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            return dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS * 12;
        });
    }

    public Scene createScene() {
        root = new StackPane();
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

        backgroundCanvas = new Canvas();
        backgroundCanvas.widthProperty().bind(root.widthProperty());
        backgroundCanvas.heightProperty().bind(root.heightProperty());
        backgroundCanvas.getStyleClass().add("background-canvas");

        // Draw static elements on background canvas
        backgroundCanvas.getGraphicsContext2D().drawImage(ResourceManager.getWaterTexture(), 0, 0);
        root.getChildren().add(0, backgroundCanvas); // Add behind game canvas

        return new Scene(root);
    }
}
