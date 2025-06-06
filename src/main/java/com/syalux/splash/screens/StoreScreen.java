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
import javafx.scene.text.TextAlignment; // Added for label text alignment

public class StoreScreen {

    private int currentIndex = 0;
    private Label coinsLabel;
    private ImageView characterImage; // Renamed from fishImage for broader character support
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

        // Top Bar: Title and Coins
        HBox topBar = new HBox(50); // Increased spacing
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(30, 50, 20, 50)); // Adjusted padding for better visual balance
        topBar.getStyleClass().add("store-top-bar"); // New style class for top bar

        Label storeTitle = new Label(Resource.getString("store_title")); // Assuming you'll add "store_title" to Resource.properties
        storeTitle.getStyleClass().add("store-title");
        
        coinsLabel = new Label(Resource.getString("coins") + ": " + profile.getCoins());
        coinsLabel.getStyleClass().add("coins-label");

        Region spacer = new Region(); // Spacer to push coins to the right
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(storeTitle, spacer, coinsLabel);
        root.setTop(topBar);

        // Center Content: Character Card with Navigation Buttons
        StackPane centerWrapper = new StackPane(); // Use StackPane to overlay nav buttons
        centerWrapper.setAlignment(Pos.CENTER);
        
        VBox characterCard = new VBox(20); // Spacing inside the card
        characterCard.getStyleClass().add("character-card"); // New, dedicated style for the main card
        characterCard.setAlignment(Pos.CENTER);
        characterCard.setMaxWidth(600); // Max width for the card
        characterCard.setPadding(new Insets(30)); // Padding inside the card

        characterImage = new ImageView();
        characterImage.setPreserveRatio(true);
        characterImage.setFitWidth(400); // Max width for image within its card
        characterImage.setFitHeight(400); // Ensure image isn't too tall

        nameLabel = new Label();
        nameLabel.getStyleClass().add("character-name"); // Changed from fish-name
        nameLabel.setTextAlignment(TextAlignment.CENTER);

        priceLabel = new Label();
        priceLabel.getStyleClass().add("character-price"); // Changed from fish-price

        statsLabel = new Label();
        statsLabel.getStyleClass().add("character-stats"); // Changed from fish-stats
        statsLabel.setWrapText(true);
        statsLabel.setTextAlignment(TextAlignment.CENTER);

        actionButton = new Button();
        actionButton.getStyleClass().add("buy-button"); // Explicitly use buy-button style
        actionButton.setOnAction(e -> handleCharacterAction());

        characterCard.getChildren().addAll(characterImage, nameLabel, statsLabel, priceLabel, actionButton);
        StackPane.setAlignment(characterCard, Pos.CENTER); // Center the card within the wrapper

        Button prevButton = new Button("<");
        prevButton.getStyleClass().add("nav-button");
        prevButton.setOnAction(e -> navigate(-1));
        StackPane.setAlignment(prevButton, Pos.CENTER_LEFT); // Position left
        StackPane.setMargin(prevButton, new Insets(0, 0, 0, 50)); // Margin from left edge

        Button nextButton = new Button(">");
        nextButton.getStyleClass().add("nav-button");
        nextButton.setOnAction(e -> navigate(1));
        StackPane.setAlignment(nextButton, Pos.CENTER_RIGHT); // Position right
        StackPane.setMargin(nextButton, new Insets(0, 50, 0, 0)); // Margin from right edge

        centerWrapper.getChildren().addAll(characterCard, prevButton, nextButton);
        root.setCenter(centerWrapper);

        // Bottom Bar: Back Button
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(20, 0, 30, 0)); // Adjusted padding
        
        Button backButton = new Button(Resource.getString("back"));
        backButton.getStyleClass().add("menu-button"); // Using menu-button for a consistent large button style
        backButton.setOnAction(e -> Manager.showMainMenu());
        bottomBar.getChildren().add(backButton);
        root.setBottom(bottomBar);

        updateDisplay();
        root.getStylesheets().add(Resource.getStyleSheet()); // Ensure stylesheet is applied
        return root;
    }

    private void updateDisplay() {
        CharacterData character = CharacterManager.getAllCharacters().get(currentIndex);

        characterImage.setImage(Resource.getFishImage(character.getId()));
        nameLabel.setText(character.getName());
        
        // Use a more readable format for stats, potentially with icons later if needed
        statsLabel.setText(String.format(
                "%s: %d\n%s: %d\n%s: %d",
                Resource.getString("health"), character.getBaseHealth(),
                Resource.getString("speed"), character.getBaseSpeed(),
                Resource.getString("size"), character.getBaseSize()
        ));

        coinsLabel.setText(Resource.getString("coins") + ": " + profile.getCoins());

        if (character.isUnlocked()) {
            actionButton.setText(profile.getFishType() == character.getId() ?
                    Resource.getString("selected") : Resource.getString("select"));
            actionButton.setDisable(profile.getFishType() == character.getId());
            priceLabel.setText(Resource.getString("unlocked")); // Indicate it's unlocked
        } else {
            actionButton.setText(Resource.getString("buy")); // Shorter text for button
            priceLabel.setText(Resource.getString("price") + ": " + character.getPrice());
            actionButton.setDisable(profile.getCoins() < character.getPrice());
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
                character.setUnlocked(true); // Update the character's unlocked status
                Manager.setProfile(profile);
            }
        } else {
            profile.setFishType(character.getId());
            Manager.setProfile(profile);
        }
        updateDisplay();
    }
}