package com.syalux.splash.screens;

import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SettingsScreen {
    private Slider masterVolumeSlider;
    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;
    private CheckBox fullscreenToggle;
    private ComboBox<String> userDifficultyCombo; // Renamed for clarity
    private ComboBox<String> resolutionCombo;
    private CheckBox showFpsToggle;
    private Slider cameraSensitivitySlider;

    // Game Configuration Controls (directly map to Config class)
    private Slider spawnRadiusSlider;
    private Slider despawnRadiusSlider;
    private Slider gameDifficultyFactorSlider; // Renamed to avoid confusion with user preference difficulty
    private Slider spawnDurationSlider;
    private Slider depthDivisorSlider;
    private Slider maxDepthAlphaSlider;

    /**
     * Creates and returns the root Parent node for the settings screen.
     * @return The BorderPane representing the settings screen.
     */
    public Parent createRoot() {
        // Main container
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("settings-container");
        mainLayout.setPrefSize(Manager.getPrimaryStage().getWidth(), Manager.getPrimaryStage().getHeight());

        // Top section with title and back button
        HBox topBar = createTopBar();
        mainLayout.setTop(topBar);

        // Center content with tabs for different setting categories
        TabPane tabPane = createTabPane();
        mainLayout.setCenter(tabPane);

        // Bottom section with save and reset buttons
        HBox bottomBar = createBottomBar();
        mainLayout.setBottom(bottomBar);

        return mainLayout;
    }

    /**
     * Creates the top bar containing the back button and screen title.
     * @return HBox for the top bar.
     */
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("settings-top-bar");

        Button backButton = new Button("←");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> Manager.goBack());

        Text title = new Text(Resource.getString("settings"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.getStyleClass().add("settings-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Push content to left/right

        topBar.getChildren().addAll(backButton, title, spacer);
        return topBar;
    }

    /**
     * Creates the tab pane to categorize settings (Audio, Video, Gameplay, Game Config).
     * @return TabPane with various setting tabs.
     */
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("settings-tab-pane");

        Tab audioTab = new Tab(Resource.getString("volume_title"));
        audioTab.setContent(createAudioTab());
        audioTab.setClosable(false);

        Tab videoTab = new Tab(Resource.getString("video_settings"));
        videoTab.setContent(createVideoTab());
        videoTab.setClosable(false);

        Tab gameplayTab = new Tab(Resource.getString("gameplay_settings"));
        gameplayTab.setContent(createGameplayTab());
        gameplayTab.setClosable(false);

        Tab configTab = new Tab(Resource.getString("game_config"));
        configTab.setContent(createConfigTab());
        configTab.setClosable(false);

        tabPane.getTabs().addAll(audioTab, videoTab, gameplayTab, configTab);
        return tabPane;
    }

    /**
     * Creates the UI for audio settings, affecting the user Profile.
     * @return GridPane for audio settings.
     */
    private GridPane createAudioTab() {
        GridPane grid = createSettingsGrid();

        // Master Volume
        masterVolumeSlider = createVolumeSlider(Config.MASTER_VOLUME);
        grid.add(createSettingLabel("master_volume"), 0, 0);
        grid.add(masterVolumeSlider, 1, 0);
        grid.add(createVolumeValueLabel(masterVolumeSlider), 2, 0);

        // Music Volume
        musicVolumeSlider = createVolumeSlider(Config.MUSIC_VOLUME);
        grid.add(createSettingLabel("music_volume"), 0, 1);
        grid.add(musicVolumeSlider, 1, 1);
        grid.add(createVolumeValueLabel(musicVolumeSlider), 2, 1);

        // SFX Volume
        sfxVolumeSlider = createVolumeSlider(Config.SFX_VOLUME);
        grid.add(createSettingLabel("sfx_volume"), 0, 2);
        grid.add(sfxVolumeSlider, 1, 2);
        grid.add(createVolumeValueLabel(sfxVolumeSlider), 2, 2);

        return grid;
    }

    /**
     * Creates the UI for video settings, affecting the user Profile.
     * @return GridPane for video settings.
     */
    private GridPane createVideoTab() {
        GridPane grid = createSettingsGrid();

        // Resolution
        resolutionCombo = new ComboBox<>(FXCollections.observableArrayList(
                "1920x1080", "1600x900", "1366x768", "1280x720", "1024x768"
        ));
        resolutionCombo.setValue(Config.RESOLUTION);
        resolutionCombo.setMinWidth(200);
        grid.add(createSettingLabel("resolution"), 0, 0);
        grid.add(resolutionCombo, 1, 0);

        // Fullscreen
        fullscreenToggle = new CheckBox();
        fullscreenToggle.setSelected(Config.FULLSCREEN);
        grid.add(createSettingLabel("fullscreen"), 0, 1);
        grid.add(fullscreenToggle, 1, 1);

        // Show FPS
        showFpsToggle = new CheckBox();
        showFpsToggle.setSelected(Config.SHOW_FPS);
        grid.add(createSettingLabel("show_fps"), 0, 2);
        grid.add(showFpsToggle, 1, 2);

        return grid;
    }

    /**
     * Creates the UI for gameplay settings, affecting the user Profile.
     * @return GridPane for gameplay settings.
     */
    private GridPane createGameplayTab() {
        GridPane grid = createSettingsGrid();

        // Difficulty (User preference, not game mechanics difficulty)
        userDifficultyCombo = new ComboBox<>(FXCollections.observableArrayList(
                Resource.getString("easy"),
                Resource.getString("normal"),
                Resource.getString("hard")
        ));
        userDifficultyCombo.setValue(Config.USER_DIFFICULTY);
        grid.add(createSettingLabel("difficulty"), 0, 0);
        grid.add(userDifficultyCombo, 1, 0);

        // Camera Sensitivity
        cameraSensitivitySlider = new Slider(0.1, 2.0, Config.CAMERA_SENSITIVITY);
        cameraSensitivitySlider.setShowTickMarks(true);
        cameraSensitivitySlider.setShowTickLabels(true);
        cameraSensitivitySlider.setMajorTickUnit(0.5);
        cameraSensitivitySlider.setBlockIncrement(0.1);
        cameraSensitivitySlider.setSnapToTicks(true);
        grid.add(createSettingLabel("camera_sensitivity"), 0, 1);
        grid.add(cameraSensitivitySlider, 1, 1);
        grid.add(createValueLabel(cameraSensitivitySlider, "%.1f"), 2, 1);

        return grid;
    }

    /**
     * Creates the UI for game configuration settings, directly modifying the Config class.
     * @return GridPane for game configuration settings.
     */
    private GridPane createConfigTab() {
        GridPane grid = createSettingsGrid();

        // Spawn Radius
        spawnRadiusSlider = new Slider(500, 5000, Config.SPAWN_RADIUS);
        spawnRadiusSlider.setShowTickMarks(true);
        spawnRadiusSlider.setShowTickLabels(true);
        spawnRadiusSlider.setMajorTickUnit(1000);
        spawnRadiusSlider.setBlockIncrement(100);
        grid.add(createSettingLabel("spawn_radius"), 0, 0);
        grid.add(spawnRadiusSlider, 1, 0);
        grid.add(createValueLabel(spawnRadiusSlider, "%.0f"), 2, 0);

        // Despawn Radius
        despawnRadiusSlider = new Slider(1000, 10000, Config.DESPAWN_RADIUS);
        despawnRadiusSlider.setShowTickMarks(true);
        despawnRadiusSlider.setShowTickLabels(true);
        despawnRadiusSlider.setMajorTickUnit(2000);
        despawnRadiusSlider.setBlockIncrement(500);
        grid.add(createSettingLabel("despawn_radius"), 0, 1);
        grid.add(despawnRadiusSlider, 1, 1);
        grid.add(createValueLabel(despawnRadiusSlider, "%.0f"), 2, 1);

        // Difficulty Factor (affects game mechanics, not user preference)
        gameDifficultyFactorSlider = new Slider(0.1, 3.0, Config.GAME_DIFFICULTY_FACTOR);
        gameDifficultyFactorSlider.setShowTickMarks(true);
        gameDifficultyFactorSlider.setShowTickLabels(true);
        gameDifficultyFactorSlider.setMajorTickUnit(0.5);
        gameDifficultyFactorSlider.setBlockIncrement(0.1);
        grid.add(createSettingLabel("difficulty_factor"), 0, 2);
        grid.add(gameDifficultyFactorSlider, 1, 2);
        grid.add(createValueLabel(gameDifficultyFactorSlider, "%.1f"), 2, 2);

        // Spawn Duration
        spawnDurationSlider = new Slider(0.01, 1.0, Config.SPAWN_DURATION_SECONDS);
        spawnDurationSlider.setShowTickMarks(true);
        spawnDurationSlider.setShowTickLabels(true);
        spawnDurationSlider.setMajorTickUnit(0.2);
        spawnDurationSlider.setBlockIncrement(0.05);
        grid.add(createSettingLabel("spawn_duration"), 0, 3);
        grid.add(spawnDurationSlider, 1, 3);
        grid.add(createValueLabel(spawnDurationSlider, "%.2f"), 2, 3);

        // Depth Divisor
        depthDivisorSlider = new Slider(1000, 20000, Config.DEPTH_DIVISOR);
        depthDivisorSlider.setShowTickMarks(true);
        depthDivisorSlider.setShowTickLabels(true);
        depthDivisorSlider.setMajorTickUnit(5000);
        depthDivisorSlider.setBlockIncrement(1000);
        grid.add(createSettingLabel("depth_divisor"), 0, 4);
        grid.add(depthDivisorSlider, 1, 4);
        grid.add(createValueLabel(depthDivisorSlider, "%.0f"), 2, 4);

        // Max Depth Alpha
        maxDepthAlphaSlider = new Slider(0.1, 1.0, Config.MAX_DEPTH_ALPHA);
        maxDepthAlphaSlider.setShowTickMarks(true);
        maxDepthAlphaSlider.setShowTickLabels(true);
        maxDepthAlphaSlider.setMajorTickUnit(0.2);
        maxDepthAlphaSlider.setBlockIncrement(0.05);
        grid.add(createSettingLabel("max_depth_alpha"), 0, 5);
        grid.add(maxDepthAlphaSlider, 1, 5);
        grid.add(createValueLabel(maxDepthAlphaSlider, "%.2f"), 2, 5);

        return grid;
    }

    /**
     * Creates the bottom bar with reset and save buttons.
     * @return HBox for the bottom bar.
     */
    private HBox createBottomBar() {
        HBox bottomBar = new HBox(20);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.getStyleClass().add("settings-bottom-bar");

        Button resetButton = new Button(Resource.getString("reset_defaults"));
        resetButton.getStyleClass().add("settings-button");
        resetButton.setOnAction(e -> resetToDefaults());

        Button saveButton = new Button(Resource.getString("save_settings"));
        saveButton.getStyleClass().add("settings-button");
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(e -> saveSettings());

        bottomBar.getChildren().addAll(resetButton, saveButton);
        return bottomBar;
    }

    /**
     * Helper method to create a standard GridPane for settings.
     * @return A pre-configured GridPane.
     */
    private GridPane createSettingsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(25));
        return grid;
    }

    /**
     * Helper method to create a volume slider with common properties.
     * @param value The initial value of the slider.
     * @return Configured Slider.
     */
    private Slider createVolumeSlider(double value) {
        Slider slider = new Slider(0, 100, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(5);
        slider.setSnapToTicks(true);
        slider.getStyleClass().add("volume-slider");
        return slider;
    }

    /**
     * Helper method to create a label for volume values (e.g., "80%").
     * @param slider The slider whose value the label will display.
     * @return Label displaying the volume percentage.
     */
    private Label createVolumeValueLabel(Slider slider) {
        return createValueLabel(slider, "%.0f%%");
    }

    /**
     * Helper method to create a label that dynamically displays a slider's value.
     * @param slider The slider whose value the label will display.
     * @param format The format string for the displayed value (e.g., "%.1f").
     * @return Label displaying the formatted slider value.
     */
    private Label createValueLabel(Slider slider, String format) {
        Label label = new Label();
        label.textProperty().bind(Bindings.createStringBinding(() ->
                String.format(format, slider.getValue()),
                slider.valueProperty()
        ));
        label.getStyleClass().add("value-label"); // Consistent style class
        label.setMinWidth(80);
        return label;
    }

    /**
     * Helper method to create a setting label with a consistent style.
     * @param resourceKey The key to retrieve the label text from Resource bundle.
     * @return Label for a setting.
     */
    private Label createSettingLabel(String resourceKey) {
        Label label = new Label(Resource.getString(resourceKey) + ":");
        label.getStyleClass().add("setting-label");
        label.setMinWidth(200);
        return label;
    }

    /**
     * Saves the current settings from the UI controls to both the Profile and Config classes,
     * then applies immediate display settings and persists them to disk.
     */
    private void saveSettings() {
        // Save user preferences to Config (static fields)
        Config.MASTER_VOLUME = masterVolumeSlider.getValue();
        Config.MUSIC_VOLUME = musicVolumeSlider.getValue();
        Config.SFX_VOLUME = sfxVolumeSlider.getValue();
        Config.RESOLUTION = resolutionCombo.getValue();
        Config.FULLSCREEN = fullscreenToggle.isSelected();
        Config.SHOW_FPS = showFpsToggle.isSelected();
        Config.USER_DIFFICULTY = userDifficultyCombo.getValue();
        Config.CAMERA_SENSITIVITY = cameraSensitivitySlider.getValue();

        // Save game configuration to Config (static fields)
        Config.SPAWN_RADIUS = spawnRadiusSlider.getValue();
        Config.DESPAWN_RADIUS = despawnRadiusSlider.getValue();
        Config.GAME_DIFFICULTY_FACTOR = gameDifficultyFactorSlider.getValue(); // Use renamed slider
        Config.SPAWN_DURATION_SECONDS = spawnDurationSlider.getValue();
        Config.DEPTH_DIVISOR = depthDivisorSlider.getValue();
        Config.MAX_DEPTH_ALPHA = maxDepthAlphaSlider.getValue();

        // Apply display-related settings immediately
        applyDisplaySettings();

        // Persist settings to disk
        // Manager.setProfile(profile); // Profile no longer holds these settings, so no need to save profile for this
        Config.saveConfig();         // Saves the Config to file

        // Show confirmation to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Resource.getString("settings_saved"));
        alert.setHeaderText(null);
        alert.setContentText(Resource.getString("settings_saved_message"));
        alert.showAndWait();
    }

    /**
     * Resets all settings controls in the UI to their default values.
     * Note: This does NOT save or apply the settings automatically.
     */
    private void resetToDefaults() {
        // Reset user preferences sliders/toggles/combos (now updating Config's static fields)
        Config.MASTER_VOLUME = 80.0;
        Config.MUSIC_VOLUME = 70.0;
        Config.SFX_VOLUME = 90.0;
        Config.RESOLUTION = "1920x1080";
        Config.FULLSCREEN = true;
        Config.SHOW_FPS = false;
        Config.USER_DIFFICULTY = Resource.getString("normal");
        Config.CAMERA_SENSITIVITY = 1.0;

        // Apply reset values to UI controls immediately
        masterVolumeSlider.setValue(Config.MASTER_VOLUME);
        musicVolumeSlider.setValue(Config.MUSIC_VOLUME);
        sfxVolumeSlider.setValue(Config.SFX_VOLUME);
        resolutionCombo.setValue(Config.RESOLUTION);
        fullscreenToggle.setSelected(Config.FULLSCREEN);
        showFpsToggle.setSelected(Config.SHOW_FPS);
        userDifficultyCombo.setValue(Config.USER_DIFFICULTY);
        cameraSensitivitySlider.setValue(Config.CAMERA_SENSITIVITY);

        // Reset game configuration sliders (now updating Config's static fields)
        Config.SPAWN_RADIUS = 2000.0;
        Config.DESPAWN_RADIUS = 4000.0;
        Config.GAME_DIFFICULTY_FACTOR = 1.0;
        Config.SPAWN_DURATION_SECONDS = 0.1;
        Config.DEPTH_DIVISOR = 10000.0;
        Config.MAX_DEPTH_ALPHA = 0.95;

        // Apply reset values to UI controls immediately
        spawnRadiusSlider.setValue(Config.SPAWN_RADIUS);
        despawnRadiusSlider.setValue(Config.DESPAWN_RADIUS);
        gameDifficultyFactorSlider.setValue(Config.GAME_DIFFICULTY_FACTOR);
        spawnDurationSlider.setValue(Config.SPAWN_DURATION_SECONDS);
        depthDivisorSlider.setValue(Config.DEPTH_DIVISOR);
        maxDepthAlphaSlider.setValue(Config.MAX_DEPTH_ALPHA);
    }

    /**
     * Applies display-specific settings (resolution, fullscreen) to the primary stage.
     */
    private void applyDisplaySettings() {
        // Apply resolution
        String[] res = Config.RESOLUTION.split("x");
        if (res.length == 2) {
            try {
                int width = Integer.parseInt(res[0]);
                int height = Integer.parseInt(res[1]);
                // Only set width/height if not in fullscreen to avoid conflicts
                if (!Config.FULLSCREEN) {
                    Manager.getPrimaryStage().setWidth(width);
                    Manager.getPrimaryStage().setHeight(height);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid resolution format in config: " + Config.RESOLUTION);
            }
        }

        // Apply fullscreen
        Manager.getPrimaryStage().setFullScreen(Config.FULLSCREEN);
    }
}