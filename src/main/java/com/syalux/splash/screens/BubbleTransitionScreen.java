package com.syalux.splash.screens;

import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

import com.syalux.splash.data.Config;

public class BubbleTransitionScreen extends Pane {
    private static final int BUBBLE_COUNT = 300;
    private final Random random = new Random();

    public BubbleTransitionScreen(Runnable onFinished) {
        createBubbles();
        setupAnimation(onFinished);
    }

    private void createBubbles() {
        for (int i = 0; i < BUBBLE_COUNT; i++) {
            Circle bubble = new Circle();
            bubble.setFill(Color.rgb(255, 255, 255, 0.7));
            bubble.setRadius(50 + random.nextInt(100));
            bubble.setLayoutX(random.nextDouble() * Config.GAME_WIDTH);
            bubble.setLayoutY(Config.GAME_HEIGHT + random.nextInt(200));
            this.getChildren().add(bubble);
        }
    }

    private void setupAnimation(Runnable onFinished) {
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < this.getChildren().size(); i++) {
            Circle bubble = (Circle) this.getChildren().get(i);
            
            TranslateTransition move = new TranslateTransition(
                Duration.seconds(5 + random.nextDouble() * 2), 
                bubble
            );
            move.setByY(-Config.GAME_HEIGHT - bubble.getRadius() * 10);
            move.setInterpolator(Interpolator.EASE_OUT);
            
            FadeTransition fade = new FadeTransition(
                Duration.seconds(3), 
                bubble
            );
            fade.setFromValue(0.7);
            fade.setToValue(0);
            
            ScaleTransition scale = new ScaleTransition(
                Duration.seconds(5), 
                bubble
            );
            scale.setToX(0.5);
            scale.setToY(0.5);
            
            timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(i * 0.01),
                e -> new ParallelTransition(move, fade, scale).play()
            ));
        }
        
        timeline.setOnFinished(e -> {
            this.setVisible(false);
            onFinished.run();
        });
        timeline.play();
    }
}