package com.syalux.splash.screens;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.syalux.splash.core.Engine;
import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Profile;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import com.syalux.splash.entities.PlayerEntity;

public class GameScreen {

    private final PlayerEntity player;
    private final Engine engine;
    private StackPane root;
    private boolean isPaused = false;
    private boolean isDead = false;

    private Canvas gameCanvas;
    private Canvas backgroundCanvas;

    private HBox topLeftHudContainer;
    private Label scoreLabel;
    private Button pauseButton;

    public GameScreen(Profile profile) {
        player = new PlayerEntity(profile);
        this.gameCanvas = new Canvas(Config.GAME_WIDTH, Config.GAME_HEIGHT);
        this.engine = new Engine(player, gameCanvas, this::togglePause);
    }

    /**
     * Creates the HUD elements for the top-left section (health and coins).
     */
    private HBox createTopLeftHud() {
        HBox container = new HBox(40);
        container.setPadding(new Insets(30, 30, 0, 30));
        container.setAlignment(Pos.TOP_LEFT);

        HBox healthBar = createHealthBar(player.healthProperty(), player.getInitialMaxHealth(),
                player.getInitialMaxHealth() + 100);
        HBox coinDisplay = createCoinDisplay(player.coinsProperty());

        container.getChildren().addAll(healthBar, coinDisplay);
        container.getStyleClass().add("hud");

        return container;
    }

    /**
     * Creates a custom health bar with a background representing max health
     * and a foreground representing current health. The bar has a fixed visual
     * width.
     *
     * @param currentHealthProperty The IntegerProperty for the player's current
     *                              health.
     * @param maxHealth             The maximum health value for the player.
     * @param barVisualWidth        The fixed visual width (in pixels) for the
     *                              health bar.
     * @return An HBox containing the health bar.
     */
    private HBox createHealthBar(IntegerProperty currentHealthProperty, int maxHealth, double barVisualWidth) {
        HBox healthBarContainer = new HBox(10);
        healthBarContainer.setAlignment(Pos.TOP_LEFT); // Ensures alignment of this box within the top-left HUD

        StackPane barStack = new StackPane();
        barStack.setAlignment(Pos.TOP_LEFT);
        barStack.setPrefHeight(10);
        barStack.setPrefWidth(barVisualWidth);
        barStack.getStyleClass().add("health-bar-stack");

        Rectangle backgroundRect = new Rectangle();
        backgroundRect.setHeight(25);
        backgroundRect.widthProperty().bind(barStack.prefWidthProperty());
        backgroundRect.setFill(Color.web("#3a3a3a"));
        backgroundRect.setArcWidth(15);
        backgroundRect.setArcHeight(15);
        backgroundRect.getStyleClass().add("health-bar-background");

        Rectangle currentHealthRect = new Rectangle();
        currentHealthRect.setHeight(25);
        currentHealthRect.setFill(Color.web("#FF0000"));
        currentHealthRect.setArcWidth(15);
        currentHealthRect.setArcHeight(15);
        currentHealthRect.getStyleClass().add("health-bar-current");

        currentHealthRect.widthProperty().bind(
                Bindings.createDoubleBinding(
                        () -> ((double) currentHealthProperty.get() / maxHealth) * barVisualWidth,
                        currentHealthProperty));

        barStack.getChildren().addAll(backgroundRect, currentHealthRect);
        healthBarContainer.getChildren().addAll(barStack);

        return healthBarContainer;
    }

    /**
     * Creates a display for the player's coins, including a coin image and the coin
     * count.
     *
     * @param coinsProperty The IntegerProperty for the player's current coins.
     * @return An HBox containing the coin image and coin count label.
     */
    private HBox createCoinDisplay(IntegerProperty coinsProperty) {
        HBox coinDisplayBox = new HBox(8);
        coinDisplayBox.getStyleClass().add("coin-display-container");

        ImageView coinImageView = new ImageView(Resource.getCoinImage());
        coinImageView.setFitWidth(28);
        coinImageView.setFitHeight(28);
        coinImageView.setPreserveRatio(true);
        coinImageView.getStyleClass().add("coin-icon");

        Label coinCountLabel = new Label();
        coinCountLabel.textProperty().bind(coinsProperty.asString());
        coinCountLabel.getStyleClass().add("coin-count-label");

        coinDisplayBox.getChildren().addAll(coinImageView, coinCountLabel);
        return coinDisplayBox;
    }

    private Label createBoundLabel(String key, IntegerProperty property) {
        Label label = new Label();
        label.textProperty().bind(
                Bindings.createStringBinding(
                        () -> Resource.getString(key) + ": " + property.get(),
                        property,
                        Resource.currentLocaleProperty()));
        label.getStyleClass().add("hud-label");
        return label;
    }

    private Button createPauseButton() {
        Button pauseButton = new Button();
        pauseButton.textProperty().bind(
                Bindings.createStringBinding(() -> Resource.getString("pause"), Resource.currentLocaleProperty()));
        pauseButton.setOnAction(e -> togglePause());
        pauseButton.getStyleClass().add("hud-pause-button");
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
        engine.pause(isPaused);
        applyBlurEffect(isPaused);
        showPauseScreen();
    }

    private void resumeGame() {
        isPaused = false;
        engine.pause(isPaused);
        applyBlurEffect(isPaused);
        hidePauseScreen();
    }

    private void applyBlurEffect(boolean apply) {
        GaussianBlur blur = new GaussianBlur(10);
        if (gameCanvas != null)
            gameCanvas.setEffect(apply ? blur : null);
        if (backgroundCanvas != null)
            backgroundCanvas.setEffect(apply ? blur : null);
    }

    private void showPauseScreen() {
        PauseScreen pauseScreen = new PauseScreen(this::resumeGame);
        root.getChildren().add(pauseScreen);
    }

    private void hidePauseScreen() {
        root.getChildren().removeIf(node -> node instanceof PauseScreen);
    }

    /**
     * Creates and initializes the root UI element for the game screen,
     * setting up the game canvas, background, HUD, and game engine.
     * It also sets up listeners for player health and scene changes
     * to manage game state like death and saving profile data.
     *
     * @return The initialized StackPane representing the root of the game screen.
     */
    public Parent createRoot() {
        root = new StackPane();
        root.getStyleClass().add("game-container");

        backgroundCanvas = new Canvas(Config.GAME_WIDTH, Config.GAME_HEIGHT);
        backgroundCanvas.getStyleClass().add("background-canvas");
        backgroundCanvas.widthProperty().bind(root.widthProperty());
        backgroundCanvas.heightProperty().bind(root.heightProperty());

        gameCanvas.getStyleClass().add("game-canvas");
        gameCanvas.widthProperty().bind(root.widthProperty());
        gameCanvas.heightProperty().bind(root.heightProperty());

        topLeftHudContainer = createTopLeftHud();
        scoreLabel = createBoundLabel("score", player.scoreProperty());
        scoreLabel.getStyleClass().add("score-label");
        pauseButton = createPauseButton();

        root.getChildren().addAll(backgroundCanvas, gameCanvas, topLeftHudContainer, scoreLabel, pauseButton);
        root.getStylesheets().add(Resource.getStyleSheet());

        StackPane.setAlignment(topLeftHudContainer, Pos.TOP_LEFT);
        StackPane.setAlignment(scoreLabel, Pos.TOP_CENTER);
        StackPane.setAlignment(pauseButton, Pos.TOP_RIGHT);
        StackPane.setMargin(pauseButton, new Insets(20, 20, 0, 0));

        engine.setRootContainer(root);
        root.requestFocus();
        engine.start();

        player.healthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() <= 0 && !isDead) {
                isDead = true;
                showDeathScreen();
            }
        });

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                engine.pause(true);
                Profile currentProfile = Manager.getProfile();
                currentProfile.setCoins(player.getCoins());
                if (player.getScore() > currentProfile.getHighScore()) {
                    currentProfile.setHighScore(player.getScore());
                }
                Manager.setProfile(currentProfile);
            } else if (!isPaused) {
                engine.pause(false);
            }
        });

        return root;
    }

    private void showDeathScreen() {
        Profile currentProfile = Manager.getProfile();
        currentProfile.setCoins(player.getCoins());
        if (player.getScore() > currentProfile.getHighScore()) {
            currentProfile.setHighScore(player.getScore());
        }
        Manager.setProfile(currentProfile);

        applyBlurEffect(true);
        DeathScreen deathScreen = new DeathScreen(
                player.getKiller(),
                player.scoreProperty().get());
        root.getChildren().add(deathScreen);
    }
}
