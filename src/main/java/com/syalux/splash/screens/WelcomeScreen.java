package com.syalux.splash.screens;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;

public class WelcomeScreen extends StackPane {
    public WelcomeScreen() {
        createLoadingScreen();
    }

    private void createLoadingScreen() {
        this.setStyle("-fx-background-color: #1a1a1a;");
        
        // Loading spinner
        Circle spinner = new Circle(30);
        spinner.setStroke(Color.WHITE);
        spinner.setFill(Color.TRANSPARENT);
        spinner.setStrokeWidth(3);
        
        // Loading text
        Text loadingText = new Text("Loading...");
        loadingText.setFont(Font.font(20));
        loadingText.setFill(Color.WHITE);
        
        // Animations
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), spinner);
        rotate.setByAngle(360);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        
        FadeTransition fadeText = new FadeTransition(Duration.seconds(1), loadingText);
        fadeText.setFromValue(0.5);
        fadeText.setToValue(1);
        fadeText.setCycleCount(FadeTransition.INDEFINITE);
        fadeText.setAutoReverse(true);
        
        this.getChildren().addAll(spinner, loadingText);
        StackPane.setAlignment(spinner, Pos.CENTER);
        StackPane.setAlignment(loadingText, Pos.CENTER);
        // StackPane does not support setSpacing; consider using VBox if spacing is needed
        
        new ParallelTransition(rotate, fadeText).play();
    }
}