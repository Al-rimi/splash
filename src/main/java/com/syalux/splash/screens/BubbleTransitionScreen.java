package com.syalux.splash.screens;

import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import com.syalux.splash.data.Resource.Environment;

public class BubbleTransitionScreen extends Pane {
    private final Random random = new Random();

    public BubbleTransitionScreen(Runnable onFinished) {
        setupAnimation(onFinished);
    }

    /**
     * Sets up the bubble animation for the transition screen.
     * Creates and animates multiple bubbles, executing a callback when all are finished.
     *
     * @param onFinished The Runnable to execute when all bubble animations are complete.
     */
    private void setupAnimation(Runnable onFinished) {
        AtomicInteger completedCount = new AtomicInteger(0);
        int bubbleCount = 100;

        for (int i = 0; i < bubbleCount; i++) {
            double delayBeforeStart = random.nextDouble() * 1.0;
            PauseTransition delay = new PauseTransition(Duration.seconds(delayBeforeStart));

            delay.setOnFinished(e -> {
                ImageView bubble = createBubble();
                bubble.getStyleClass().add("bubble");
                getChildren().add(bubble);

                ParallelTransition transition = createBubbleAnimation(bubble);
                transition.setOnFinished(f -> {
                    getChildren().remove(bubble);
                    if (completedCount.incrementAndGet() == bubbleCount) {
                        setVisible(false);
                        onFinished.run();
                    }
                });
                transition.play();
            });
            delay.play();
        }
    }

    private ImageView createBubble() {
        ImageView bubble = new ImageView();
        int imageNumber = 1 + random.nextInt(Config.BUBBLE_IMAGE_COUNT);

        bubble.setImage(Resource.getEnvironmentImage(Environment.BUBBLE, imageNumber));
        double size = 100.0 + random.nextDouble() * (600.0 - 100.0);
        bubble.setFitWidth(size);
        bubble.setFitHeight(size);
        bubble.setPreserveRatio(true);

        bubble.setLayoutX(random.nextDouble() * Config.GAME_WIDTH - 150);
        bubble.setLayoutY(Config.GAME_HEIGHT + 50 + random.nextInt(150));

        return bubble;
    }

    private ParallelTransition createBubbleAnimation(ImageView bubble) {
        double moveDuration = 1 + random.nextDouble() * 1;

        TranslateTransition move = new TranslateTransition(Duration.seconds(moveDuration), bubble);
        move.setByY(-(Config.GAME_HEIGHT * 5));
        move.setByX((random.nextDouble() - 0.5) * 40);
        move.setInterpolator(Interpolator.LINEAR);

        FadeTransition fade = new FadeTransition(Duration.seconds(moveDuration), bubble);
        fade.setFromValue(0.4 + random.nextDouble() * 0.3);
        fade.setToValue(0.0);

        RotateTransition rotate = new RotateTransition(Duration.seconds(moveDuration), bubble);
        rotate.setByAngle(360 * (random.nextBoolean() ? 1 : -1));

        return new ParallelTransition(move, fade, rotate);
    }
}