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
import javafx.scene.text.TextAlignment;

public class StoreScreen {

    private int currentIndex = 0;
    private Label coinsLabel;
    private ImageView characterImage;
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

        HBox topBar = new HBox(50);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(30, 50, 20, 50));
        topBar.getStyleClass().add("store-top-bar");

        Label storeTitle = new Label(Resource.getString("store_title"));
        storeTitle.getStyleClass().add("store-title");
        
        coinsLabel = new Label(Resource.getString("coins") + ": " + profile.getCoins());
        coinsLabel.getStyleClass().add("coins-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(storeTitle, spacer, coinsLabel);
        root.setTop(topBar);

        StackPane centerWrapper = new StackPane();
        centerWrapper.setAlignment(Pos.CENTER);
        
        VBox characterCard = new VBox(20);
        characterCard.getStyleClass().add("character-card");
        characterCard.setAlignment(Pos.CENTER);
        characterCard.setMaxWidth(600);
        characterCard.setPadding(new Insets(30));

        characterImage = new ImageView();
        characterImage.setPreserveRatio(true);
        characterImage.setFitWidth(400);
        characterImage.setFitHeight(400);

        nameLabel = new Label();
        nameLabel.getStyleClass().add("character-name");
        nameLabel.setTextAlignment(TextAlignment.CENTER);

        priceLabel = new Label();
        priceLabel.getStyleClass().add("character-price");

        statsLabel = new Label();
        statsLabel.getStyleClass().add("character-stats");
        statsLabel.setWrapText(true);
        statsLabel.setTextAlignment(TextAlignment.CENTER);

        actionButton = new Button();
        actionButton.getStyleClass().add("buy-button");
        actionButton.setOnAction(e -> handleCharacterAction());

        characterCard.getChildren().addAll(characterImage, nameLabel, statsLabel, priceLabel, actionButton);
        StackPane.setAlignment(characterCard, Pos.CENTER);

        Button prevButton = new Button("<");
        prevButton.getStyleClass().add("nav-button");
        prevButton.setOnAction(e -> navigate(-1));
        StackPane.setAlignment(prevButton, Pos.CENTER_LEFT);
        StackPane.setMargin(prevButton, new Insets(0, 0, 0, 50));

        Button nextButton = new Button(">");
        nextButton.getStyleClass().add("nav-button");
        nextButton.setOnAction(e -> navigate(1));
        StackPane.setAlignment(nextButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(nextButton, new Insets(0, 50, 0, 0));

        centerWrapper.getChildren().addAll(characterCard, prevButton, nextButton);
        root.setCenter(centerWrapper);

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(20, 0, 30, 0));
        
        Button backButton = new Button(Resource.getString("back"));
        backButton.getStyleClass().add("menu-button");
        backButton.setOnAction(e -> Manager.showMainMenu());
        bottomBar.getChildren().add(backButton);
        root.setBottom(bottomBar);

        updateDisplay();
        root.getStylesheets().add(Resource.getStyleSheet());
        return root;
    }

    /**
     * Updates the display of the currently selected character in the store,
     * including its image, name, stats, price, and the action button state
     * (buy, select, or selected). Also updates the player's coin count.
     */
    private void updateDisplay() {
        CharacterData character = CharacterManager.getAllCharacters().get(currentIndex);

        characterImage.setImage(Resource.getFishImage(character.getId()));
        nameLabel.setText(character.getName());
        
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
            priceLabel.setText(Resource.getString("unlocked"));
        } else {
            actionButton.setText(Resource.getString("buy"));
            priceLabel.setText(Resource.getString("price") + ": " + character.getPrice());
            actionButton.setDisable(profile.getCoins() < character.getPrice());
        }
    }

    /**
     * Navigates to the next or previous character in the list based on the direction.
     * The navigation wraps around, meaning moving past the last character goes to the first,
     * and vice-versa.
     *
     * @param direction The direction to navigate: 1 for next, -1 for previous.
     */
    private void navigate(int direction) {
        int totalCharacters = CharacterManager.getAllCharacters().size();
        currentIndex = (currentIndex + direction + totalCharacters) % totalCharacters;
        updateDisplay();
    }

    /**
     * Handles the action triggered by the character button (buy or select).
     * If the character is not unlocked, it attempts to buy it if the player has enough coins.
     * If the character is already unlocked, it sets it as the player's current character.
     * The display is updated after the action.
     */
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