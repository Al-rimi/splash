package com.syalux.splash.screens;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class WelcomeScreen extends StackPane {
    private static final int ANIMATION_DURATION = 3;
    private static final String SPLASH_TEXT = "SPLASH";

    public WelcomeScreen() {
        setStyle("-fx-background-color:rgb(0, 0, 0);");
        HBox titleBox = buildTitleBox();
        getChildren().add(titleBox);
        StackPane.setAlignment(titleBox, Pos.CENTER);
        setupAutoTransition();
    }

    /**
     * Builds an HBox containing individual Text nodes for each letter of "SPLASH",
     * applying initial styling and setting up animations for each letter.
     *
     * @return An HBox containing the animated "SPLASH" title.
     */
    private HBox buildTitleBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);

        int total = SPLASH_TEXT.length();

        for (int i = 0; i < total; i++) {
            Text letter = createLetter(SPLASH_TEXT.charAt(i));
            setupLetterAnimation(letter, i, total, box);
            box.getChildren().add(letter);
        }

        return box;
    }

    private Text createLetter(char c) {
        Text letter = new Text(String.valueOf(c));
        letter.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        letter.setFill(Color.WHITE);
        letter.setOpacity(0);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(0, 150, 255, 0.3));
        letter.setEffect(glow);

        return letter;
    }

    private void setupLetterAnimation(Text letter, int index, int total, HBox titleBox) {
        Duration delay = Duration.seconds(index * 0.15);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), letter);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.3), letter);
        scaleUp.setFromX(0);
        scaleUp.setFromY(0);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);
        scaleUp.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), letter);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(Interpolator.EASE_IN);

        SequentialTransition entrance = new SequentialTransition(
                new PauseTransition(delay),
                new ParallelTransition(scaleUp, scaleDown, fade));

        entrance.setOnFinished(e -> {
            playFloatingEffect(letter);
            playGlowEffect((DropShadow) letter.getEffect());

            if (index == total - 1) {
                createRippleEffect(titleBox);
            }
        });

        entrance.play();
    }

    private void playFloatingEffect(Text letter) {
        TranslateTransition floatEffect = new TranslateTransition(Duration.seconds(2), letter);
        floatEffect.setFromY(0);
        floatEffect.setToY(-10);
        floatEffect.setAutoReverse(true);
        floatEffect.setCycleCount(Animation.INDEFINITE);
        floatEffect.play();
    }

    private void playGlowEffect(DropShadow glow) {
        Timeline glowTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.radiusProperty(), 0),
                        new KeyValue(glow.spreadProperty(), 0)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(glow.radiusProperty(), 25),
                        new KeyValue(glow.spreadProperty(), 0.4)));
        glowTimeline.setAutoReverse(true);
        glowTimeline.setCycleCount(Animation.INDEFINITE);
        glowTimeline.play();
    }

    private void createRippleEffect(HBox titleBox) {
        Circle ripple = new Circle(0, Color.TRANSPARENT);
        ripple.setStroke(Color.rgb(255, 255, 255, 0.3));
        ripple.setStrokeWidth(2);
        ripple.setMouseTransparent(true);

        ripple.translateXProperty().bind(titleBox.translateXProperty().add(titleBox.widthProperty().divide(2)));
        ripple.translateYProperty().bind(titleBox.translateYProperty().add(titleBox.heightProperty().divide(2)));

        getChildren().add(ripple);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(2), ripple);
        scale.setToX(10);
        scale.setToY(10);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), ripple);
        fade.setToValue(0);

        ParallelTransition rippleEffect = new ParallelTransition(scale, fade);
        rippleEffect.setCycleCount(Animation.INDEFINITE);
        rippleEffect.play();
    }

    /**
     * Sets up an automatic transition from the welcome screen to the main menu
     * after a defined animation duration. This involves fading out the welcome screen
     * and fading in the main menu.
     */
    private void setupAutoTransition() {
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(ANIMATION_DURATION - 1), e -> {
            Parent mainMenu = new MainMenuScreen().createRoot();
            mainMenu.setOpacity(0);

            StackPane container = (StackPane) getParent();
            container.getChildren().add(0, mainMenu);

            FadeTransition fadeMainMenu = new FadeTransition(Duration.seconds(1), mainMenu);
            fadeMainMenu.setToValue(1);

            FadeTransition fadeWelcome = new FadeTransition(Duration.seconds(1), this);
            fadeWelcome.setToValue(0);
            fadeWelcome.setOnFinished(ev -> container.getChildren().remove(this));

            new ParallelTransition(fadeMainMenu, fadeWelcome).play();
        }));
        delay.play();
    }
}