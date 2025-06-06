package com.syalux.splash.screens;

import com.syalux.splash.core.Manager;
import com.syalux.splash.data.CharacterData;
import com.syalux.splash.data.CharacterManager;
import com.syalux.splash.data.Profile;
import com.syalux.splash.data.Resource;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class StoreScreen {

    private int currentIndex = 0;
    private Label coinsLabel;
    private ImageView fishImage;
    private Label nameLabel;
    private Label priceLabel;
    private Label statsLabel;
    private Button actionButton;
    private Profile profile;

    public Parent createRoot() {
        profile = Manager.getProfile();
        BorderPane root = new BorderPane();
        root.setPrefSize(Manager.getPrimaryStage().getWidth(), Manager.getPrimaryStage().getHeight());
        root.getStyleClass().add("store-container");

        coinsLabel = new Label(Resource.getString("coins") + ": " + profile.getCoins());
        coinsLabel.getStyleClass().add("coins-label");
        HBox topBar = new HBox(coinsLabel);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(20));
        root.setTop(topBar);

        HBox centerContent = new HBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(50));

        VBox infoPanel = new VBox(20);
        infoPanel.setAlignment(Pos.CENTER_LEFT);
        infoPanel.setPrefWidth(400);
        
        nameLabel = new Label();
        nameLabel.getStyleClass().add("fish-name");
        
        priceLabel = new Label();
        priceLabel.getStyleClass().add("fish-price");
        
        statsLabel = new Label();
        statsLabel.getStyleClass().add("fish-stats");
        statsLabel.setWrapText(true);
        
        actionButton = new Button();
        actionButton.getStyleClass().add("action-button");
        actionButton.setOnAction(e -> handleCharacterAction());
        
        infoPanel.getChildren().addAll(nameLabel, priceLabel, statsLabel, actionButton);

        fishImage = new ImageView();
        fishImage.setPreserveRatio(true);
        fishImage.setFitWidth(500);

        Button prevButton = new Button("<");
        prevButton.getStyleClass().add("nav-button");
        prevButton.setOnAction(e -> navigate(-1));
        
        Button nextButton = new Button(">");
        nextButton.getStyleClass().add("nav-button");
        nextButton.setOnAction(e -> navigate(1));

        centerContent.getChildren().addAll(prevButton, infoPanel, fishImage, nextButton);
        root.setCenter(centerContent);

        Button backButton = new Button(Resource.getString("back"));
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> Manager.showMainMenu());
        HBox bottomBar = new HBox(backButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(20));
        root.setBottom(bottomBar);

        updateDisplay();

        return root;
    }

    private void updateDisplay() {
        CharacterData character = CharacterManager.getAllCharacters().get(currentIndex);
        
        fishImage.setImage(Resource.getFishImage(character.getId()));
        nameLabel.setText(character.getName());
        priceLabel.setText(Resource.getString("price") + ": " + character.getPrice());
        statsLabel.setText(String.format(
            "%s: %d | %s: %d | %s: %d",
            Resource.getString("health"),
            character.getBaseHealth(),
            Resource.getString("speed"),
            character.getBaseSpeed(),
            Resource.getString("size"),
            character.getBaseSize()
        ));
        
        coinsLabel.setText(Resource.getString("coins") + ": " + profile.getCoins());
        
        if (character.isUnlocked()) {
            actionButton.setText(profile.getFishType() == character.getId() ? 
                Resource.getString("selected") : Resource.getString("select"));
            actionButton.setDisable(profile.getFishType() == character.getId());
        } else {
            actionButton.setText(Resource.getString("buy") + " (" + character.getPrice() + ")");
            actionButton.setDisable(profile.getCoins() < character.getPrice());
            
            if (profile.getCoins() >= character.getPrice()) {
                actionButton.getStyleClass().remove("disabled-buy");
            } else {
                actionButton.getStyleClass().add("disabled-buy");
            }
        }
    }

    private void navigate(int direction) {
        int totalCharacters = CharacterManager.getAllCharacters().size();
        currentIndex = (currentIndex + direction + totalCharacters) % totalCharacters;
        updateDisplay();
    }

    private void handleCharacterAction() {
        CharacterData character = CharacterManager.getAllCharacters().get(currentIndex);
        
        if (!character.isUnlocked()) {
            if (profile.getCoins() >= character.getPrice()) {
                profile.setCoins(profile.getCoins() - character.getPrice());
                profile.unlockCharacter(character.getId());
                character.setUnlocked(true);
                Manager.setProfile(profile);
            }
        } else {
            profile.setFishType(character.getId());
            Manager.setProfile(profile);
        }
        
        updateDisplay();
    }
}