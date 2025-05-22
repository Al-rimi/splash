package com.syalux.splash.screens;

import com.syalux.splash.core.Config;
import com.syalux.splash.core.GameManager;
import com.syalux.splash.core.ResourceManager;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SettingsScreen {

    public Scene createScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(40, 20, 20, 20));
        mainLayout.getStyleClass().add("settings-container");
        mainLayout.setPrefSize(Config.GAME_WIDTH, Config.GAME_HEIGHT);

        VBox volumeControl = createVolumeControl();
        VBox languageSelector = createLanguageSelector();
        HBox bottomNavigation = createBottomNavigation();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        mainLayout.getChildren().addAll(
            createSectionTitle("volume_title"),
            volumeControl,
            createSectionTitle("language_title"),
            languageSelector,
            spacer,
            bottomNavigation
        );

        Scene scene = new Scene(mainLayout);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(ResourceManager.getStyleSheet());

        mainLayout.prefWidthProperty().bind(scene.widthProperty());
        mainLayout.prefHeightProperty().bind(scene.heightProperty());

        return scene;
    }

    private VBox createVolumeControl() {
        VBox volumeBox = new VBox(10);
        volumeBox.setAlignment(Pos.CENTER);
        volumeBox.getStyleClass().add("volume-container");

        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setId("volumeSlider");
        volumeSlider.getStyleClass().add("styled-slider");

        Label volumeValue = new Label();
        volumeValue.textProperty().bind(
            Bindings.concat(volumeSlider.valueProperty().asString("%.0f"), " %")
        );
        volumeValue.getStyleClass().add("volume-value");

        volumeBox.getChildren().addAll(volumeSlider, volumeValue);
        return volumeBox;
    }

    private VBox createLanguageSelector() {
        VBox langBox = new VBox(15);
        langBox.setAlignment(Pos.CENTER);
        langBox.getStyleClass().add("language-container");

        HBox buttonRow = new HBox(20);
        buttonRow.setAlignment(Pos.CENTER);

        Button[] langButtons = {
            createLanguageButton("English", "en"),
            createLanguageButton("中文", "zh"),
            createLanguageButton("العربية", "ar")
        };

        buttonRow.getChildren().addAll(langButtons);
        buttonRow.spacingProperty().bind(langBox.widthProperty().divide(20));

        langBox.getChildren().add(buttonRow);
        return langBox;
    }

    private Button createLanguageButton(String text, String langCode) {
        Button btn = new Button(text);
        btn.getStyleClass().add("lang-button");
        btn.setOnAction(e -> ResourceManager.loadLanguage(langCode));

        btn.prefWidthProperty().bind(btn.heightProperty().multiply(3));
        btn.setMinWidth(120);
        return btn;
    }

    private HBox createBottomNavigation() {
        HBox nav = new HBox();
        nav.setAlignment(Pos.CENTER_RIGHT);
        nav.getStyleClass().add("bottom-nav");

        Button btnBack = new Button();
        btnBack.getStyleClass().add("nav-button");
        btnBack.textProperty().bind(
            Bindings.createStringBinding(() ->
                ResourceManager.getString("back"),
                ResourceManager.currentLocaleProperty()
            )
        );
        btnBack.setOnAction(e -> GameManager.showMainMenu());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        nav.getChildren().addAll(spacer, btnBack);
        return nav;
    }

    private Label createSectionTitle(String resourceKey) {
        Label label = new Label();
        label.textProperty().bind(
            Bindings.createStringBinding(() ->
                ResourceManager.getString(resourceKey),
                ResourceManager.currentLocaleProperty()
            )
        );
        label.getStyleClass().add("section-title");
        return label;
    }
}
