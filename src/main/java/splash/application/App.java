package splash.application;

import javafx.application.Application;
import javafx.stage.Stage;
import splash.managers.GameManager;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameManager.init(primaryStage);
        GameManager.showMainMenu();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
