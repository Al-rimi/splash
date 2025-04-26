package splash.ui.screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import splash.managers.GameManager;
import splash.managers.ResourceManager;

public class MainMenuScreen {
    public Scene createScene() {
        VBox layout = new VBox(20);
        
        Button btnEnglish = new Button("English");
        Button btnChinese = new Button("中文");
        Button btnArabic = new Button("العربية");
        
        Button btnStart = new Button(ResourceManager.getString("start_game"));
        Button btnExit = new Button(ResourceManager.getString("exit"));
        
        // Language selection handlers
        btnEnglish.setOnAction(e -> {
            ResourceManager.loadLanguage("en");
            updateUI(btnStart, btnExit);
        });
        
        btnChinese.setOnAction(e -> {
            ResourceManager.loadLanguage("zh");
            updateUI(btnStart, btnExit);
        });
        
        btnArabic.setOnAction(e -> {
            ResourceManager.loadLanguage("ar");
            updateUI(btnStart, btnExit);
        });

        btnStart.setOnAction(e -> GameManager.startGame());
        btnExit.setOnAction(e -> System.exit(0));

        layout.getChildren().addAll(btnEnglish, btnChinese, btnArabic, btnStart, btnExit);
        return new Scene(layout, 800, 600);
    }

    private void updateUI(Button start, Button exit) {
        start.setText(ResourceManager.getString("start_game"));
        exit.setText(ResourceManager.getString("exit"));
    }
}