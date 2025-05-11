package splash.screens;

import splash.core.GameManager;
import splash.core.ResourceManager;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SettingsScreen {
    private static final String CSS_PATH = "/css/styles.css";
    
    public Scene createScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getStyleClass().add("settings-container");
        mainLayout.setPadding(new Insets(40, 20, 20, 20));
        
        // Volume Control Section
        VBox volumeBox = createVolumeControl();
        
        // Language Selection Section
        VBox langBox = createLanguageSelector();
        
        // Bottom Navigation
        HBox bottomNav = createBottomNavigation();
        
        mainLayout.getChildren().addAll(
            createSectionTitle("volume_title"),
            volumeBox,
            createSectionTitle("language_title"),
            langBox,
            new Region(), // Spacer
            bottomNav
        );
        
        VBox.setVgrow(mainLayout.getChildren().get(4), Priority.ALWAYS);
        
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        
        // Responsive layout bindings
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
            Bindings.concat(
                volumeSlider.valueProperty().asString("%.0f"),
                " %"
            )
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
            createLanguageButton("English", "en", "#3498db"),
            createLanguageButton("中文", "zh", "#e74c3c"),
            createLanguageButton("العربية", "ar", "#2ecc71")
        };
        
        buttonRow.getChildren().addAll(langButtons);
        langBox.getChildren().add(buttonRow);
        
        // Responsive layout
        buttonRow.spacingProperty().bind(langBox.widthProperty().divide(20));
        return langBox;
    }

    private Button createLanguageButton(String text, String langCode, String color) {
        Button btn = new Button(text);
        btn.getStyleClass().add("lang-button");
        btn.setOnAction(e -> ResourceManager.loadLanguage(langCode));
        
        btn.prefWidthProperty().bind(btn.heightProperty().multiply(3));
        btn.minWidth(120);
        return btn;
    }

    private HBox createBottomNavigation() {
        HBox bottomNav = new HBox();
        bottomNav.setAlignment(Pos.CENTER_RIGHT);
        bottomNav.getStyleClass().add("bottom-nav");
        
        Button btnBack = new Button();
        btnBack.getStyleClass().add("nav-button");
        btnBack.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString("back"),
                ResourceManager.currentLocaleProperty()
            )
        );

        btnBack.setOnAction(e -> GameManager.showMainMenu());
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        bottomNav.getChildren().addAll(spacer, btnBack);
        return bottomNav;
    }

    private Label createSectionTitle(String resourceKey) {
        Label title = new Label();
        title.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString(resourceKey),
                ResourceManager.currentLocaleProperty()
            )
        );
        title.getStyleClass().add("section-title");
        return title;
    }
}