package splash.screens;

import splash.managers.GameManager;
import splash.managers.ResourceManager;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class SettingsScreen {
    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        
        Label volumeLabel = new Label();
        volumeLabel.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString("volume"),
                ResourceManager.currentLocaleProperty()
            )
        );
        
        Slider volumeSlider = new Slider(0, 100, 50);
        
        VBox langBox = new VBox(10);
        addLanguageButtons(langBox);
        
        Button btnBack = new Button();
        btnBack.textProperty().bind(
            Bindings.createStringBinding(() -> 
                ResourceManager.getString("back"),
                ResourceManager.currentLocaleProperty()
            )
        );
        btnBack.setOnAction(e -> GameManager.showMainMenu());
        
        layout.getChildren().addAll(volumeLabel, volumeSlider, langBox, btnBack);
        return new Scene(layout, 800, 600);
    }

    private void addLanguageButtons(VBox container) {
        Button[] langButtons = {
            createLanguageButton("English", "en"),
            createLanguageButton("中文", "zh"),
            createLanguageButton("العربية", "ar")
        };
        container.getChildren().addAll(langButtons);
    }

    private Button createLanguageButton(String text, String langCode) {
        Button btn = new Button(text);
        btn.setOnAction(e -> ResourceManager.loadLanguage(langCode));
        return btn;
    }
}