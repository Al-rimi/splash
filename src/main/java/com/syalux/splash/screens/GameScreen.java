package com.syalux.splash.screens;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.stream.Stream;

import com.syalux.splash.core.Engine;
import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import com.syalux.splash.entities.Player;

public class GameScreen {

    private final Player player;
    private final Engine engine;
    private StackPane root;
    private boolean isPaused = false;

    private Canvas gameCanvas;
    private Canvas backgroundCanvas;
    private HBox hud;

    public GameScreen(Player player) {
        this.player = player;
        this.gameCanvas = new Canvas(Config.GAME_WIDTH, Config.GAME_HEIGHT);
        this.engine = new Engine(player, gameCanvas, this::togglePause);
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
                        () -> Resource.getString(key) + ": " + property.get(),
                        property,
                        Resource.currentLocaleProperty()));
        return label;
    }

    private Button createPauseButton() {
        Button pauseButton = new Button();
        pauseButton.textProperty().bind(
                Bindings.createStringBinding(
                        () -> Resource.getString("pause"),
                        Resource.currentLocaleProperty()));
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
                () -> Manager.showSettingsScreen(),
                () -> {
                    engine.stop();
                    Manager.showMainMenu();
                });
        root.getChildren().add(pauseScreen);
    }

    private void hidePauseScreen() {
        root.getChildren().removeIf(node -> node instanceof PauseScreen);
    }

    public Scene createScene() {
        root = new StackPane();
        root.getStyleClass().add("game-container");

        gameCanvas.getStyleClass().add("game-canvas");
        gameCanvas.widthProperty().bind(root.widthProperty());
        gameCanvas.heightProperty().bind(root.heightProperty());

        createHUD();
        root.getChildren().addAll(gameCanvas, hud);
        root.getStylesheets().add(Resource.getStyleSheet());

        engine.setRootContainer(root);
        root.requestFocus();
        engine.start();
        
        backgroundCanvas = new Canvas();
        backgroundCanvas.widthProperty().bind(root.widthProperty());
        backgroundCanvas.heightProperty().bind(root.heightProperty());
        backgroundCanvas.getStyleClass().add("background-canvas");

        // Draw static elements on background canvas
        backgroundCanvas.getGraphicsContext2D().drawImage(Resource.getWaterTexture(), 0, 0);
        root.getChildren().add(0, backgroundCanvas); // Add behind game canvas

        return new Scene(root);
    }
}
