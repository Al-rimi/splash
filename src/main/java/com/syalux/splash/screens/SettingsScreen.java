package com.syalux.splash.screens;

import com.syalux.splash.core.Manager;
import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class SettingsScreen {
    private Slider masterVolumeSlider;
    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;
    private CheckBox fullscreenToggle;
    private ComboBox<String> userDifficultyCombo;
    private ComboBox<String> resolutionCombo;
    private CheckBox showFpsToggle;
    private Slider cameraSensitivitySlider;
    private ComboBox<String> languageCombo;

    private Slider spawnRadiusSlider;
    private Slider despawnRadiusSlider;
    private Slider gameDifficultyFactorSlider;
    private Slider spawnDurationSlider;
    private Slider depthDivisorSlider;
    private Slider maxDepthAlphaSlider;

    private Text settingsTitle;
    private Tab audioTab;
    private Tab videoTab;
    private Tab gameplayTab;
    private Tab configTab;
    private Tab languageTab;
    private Button resetButton;
    private Button saveButton;

    private SimpleStringProperty masterVolumeLabelText = new SimpleStringProperty();
    private SimpleStringProperty musicVolumeLabelText = new SimpleStringProperty();
    private SimpleStringProperty sfxVolumeLabelText = new SimpleStringProperty();
    private SimpleStringProperty resolutionLabelText = new SimpleStringProperty();
    private SimpleStringProperty fullscreenLabelText = new SimpleStringProperty();
    private SimpleStringProperty showFpsLabelText = new SimpleStringProperty();
    private SimpleStringProperty difficultyLabelText = new SimpleStringProperty();
    private SimpleStringProperty cameraSensitivityLabelText = new SimpleStringProperty();
    private SimpleStringProperty languageLabelText = new SimpleStringProperty();
    private SimpleStringProperty spawnRadiusLabelText = new SimpleStringProperty();
    private SimpleStringProperty despawnRadiusLabelText = new SimpleStringProperty();
    private SimpleStringProperty gameDifficultyFactorLabelText = new SimpleStringProperty();
    private SimpleStringProperty spawnDurationLabelText = new SimpleStringProperty();
    private SimpleStringProperty depthDivisorLabelText = new SimpleStringProperty();
    private SimpleStringProperty maxDepthAlphaLabelText = new SimpleStringProperty();

    public Parent createRoot() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("settings-container");
        mainLayout.setPrefSize(Manager.getPrimaryStage().getWidth(), Manager.getPrimaryStage().getHeight());

        VBox topSection = createTopSection();
        mainLayout.setTop(topSection);

        TabPane tabPane = createTabPane();
        mainLayout.setCenter(tabPane);

        HBox bottomBar = createBottomBar();
        mainLayout.setBottom(bottomBar);

        updateLocalizedText();

        Resource.currentLocaleProperty().addListener((obs, oldLocale, newLocale) -> updateLocalizedText());

        return mainLayout;
    }

    private VBox createTopSection() {
        VBox topSection = new VBox();
        topSection.getStyleClass().add("settings-top-section");
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(30, 0, 20, 0));

        HBox topBarContent = new HBox(20);
        topBarContent.setAlignment(Pos.CENTER_LEFT);
        topBarContent.setPadding(new Insets(0, 50, 0, 50));

        Button backButton = new Button("←");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> Manager.goBack());

        settingsTitle = new Text();
        settingsTitle.getStyleClass().add("settings-title");
        settingsTitle.textProperty().bind(
                Bindings.createStringBinding(() -> Resource.getString("settings"), Resource.currentLocaleProperty()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBarContent.getChildren().addAll(backButton, spacer, settingsTitle);

        topSection.getChildren().add(topBarContent);
        return topSection;
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("settings-tab-pane");

        audioTab = new Tab();
        audioTab.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("volume_title"),
                Resource.currentLocaleProperty()));
        audioTab.setContent(createAudioTab());
        audioTab.setClosable(false);

        videoTab = new Tab();
        videoTab.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("video_settings"),
                Resource.currentLocaleProperty()));
        videoTab.setContent(createVideoTab());
        videoTab.setClosable(false);

        gameplayTab = new Tab();
        gameplayTab.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("gameplay_settings"),
                Resource.currentLocaleProperty()));
        gameplayTab.setContent(createGameplayTab());
        gameplayTab.setClosable(false);

        configTab = new Tab();
        configTab.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("game_config"),
                Resource.currentLocaleProperty()));
        configTab.setContent(createConfigTab());
        configTab.setClosable(false);

        languageTab = new Tab();
        languageTab.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("language_title"),
                Resource.currentLocaleProperty()));
        languageTab.setContent(createLanguageTab());
        languageTab.setClosable(false);

        tabPane.getTabs().addAll(audioTab, videoTab, gameplayTab, configTab, languageTab);
        return tabPane;
    }

    private GridPane createAudioTab() {
        GridPane grid = createSettingsGrid();

        masterVolumeSlider = createVolumeSlider(Config.MASTER_VOLUME);
        grid.add(createSettingLabel(masterVolumeLabelText, "master_volume"), 0, 0);
        grid.add(masterVolumeSlider, 1, 0);
        grid.add(createVolumeValueLabel(masterVolumeSlider), 2, 0);

        musicVolumeSlider = createVolumeSlider(Config.MUSIC_VOLUME);
        grid.add(createSettingLabel(musicVolumeLabelText, "music_volume"), 0, 1);
        grid.add(musicVolumeSlider, 1, 1);
        grid.add(createVolumeValueLabel(musicVolumeSlider), 2, 1);

        sfxVolumeSlider = createVolumeSlider(Config.SFX_VOLUME);
        grid.add(createSettingLabel(sfxVolumeLabelText, "sfx_volume"), 0, 2);
        grid.add(sfxVolumeSlider, 1, 2);
        grid.add(createVolumeValueLabel(sfxVolumeSlider), 2, 2);

        return grid;
    }

    private GridPane createVideoTab() {
        GridPane grid = createSettingsGrid();

        resolutionCombo = new ComboBox<>(FXCollections.observableArrayList(
                "1920x1080", "1600x900", "1366x768", "1280x720", "1024x768"));
        resolutionCombo.setValue(Config.RESOLUTION);
        resolutionCombo.setMinWidth(250);
        resolutionCombo.getStyleClass().add("combo-box");
        grid.add(createSettingLabel(resolutionLabelText, "resolution"), 0, 0);
        grid.add(resolutionCombo, 1, 0);

        fullscreenToggle = new CheckBox();
        fullscreenToggle.setSelected(Config.FULLSCREEN);
        fullscreenToggle.getStyleClass().add("check-box");
        grid.add(createSettingLabel(fullscreenLabelText, "fullscreen"), 0, 1);
        grid.add(fullscreenToggle, 1, 1);

        showFpsToggle = new CheckBox();
        showFpsToggle.setSelected(Config.SHOW_FPS);
        showFpsToggle.getStyleClass().add("check-box");
        grid.add(createSettingLabel(showFpsLabelText, "show_fps"), 0, 2);
        grid.add(showFpsToggle, 1, 2);

        return grid;
    }

    private GridPane createGameplayTab() {
        GridPane grid = createSettingsGrid();

        userDifficultyCombo = new ComboBox<>();
        userDifficultyCombo.setMinWidth(250);
        userDifficultyCombo.getStyleClass().add("combo-box");
        grid.add(createSettingLabel(difficultyLabelText, "difficulty"), 0, 0);
        grid.add(userDifficultyCombo, 1, 0);

        cameraSensitivitySlider = new Slider(0.1, 2.0, Config.CAMERA_SENSITIVITY);
        cameraSensitivitySlider.setShowTickMarks(true);
        cameraSensitivitySlider.setShowTickLabels(true);
        cameraSensitivitySlider.setMajorTickUnit(0.5);
        cameraSensitivitySlider.setBlockIncrement(0.1);
        cameraSensitivitySlider.setSnapToTicks(true);
        cameraSensitivitySlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(cameraSensitivityLabelText, "camera_sensitivity"), 0, 1);
        grid.add(cameraSensitivitySlider, 1, 1);
        grid.add(createValueLabel(cameraSensitivitySlider, "%.1f"), 2, 1);

        return grid;
    }

    private GridPane createConfigTab() {
        GridPane grid = createSettingsGrid();

        spawnRadiusSlider = new Slider(500, 5000, Config.SPAWN_RADIUS);
        spawnRadiusSlider.setShowTickMarks(true);
        spawnRadiusSlider.setShowTickLabels(true);
        spawnRadiusSlider.setMajorTickUnit(1000);
        spawnRadiusSlider.setBlockIncrement(100);
        spawnRadiusSlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(spawnRadiusLabelText, "spawn_radius"), 0, 0);
        grid.add(spawnRadiusSlider, 1, 0);
        grid.add(createValueLabel(spawnRadiusSlider, "%.0f"), 2, 0);

        despawnRadiusSlider = new Slider(1000, 10000, Config.DESPAWN_RADIUS);
        despawnRadiusSlider.setShowTickMarks(true);
        despawnRadiusSlider.setShowTickLabels(true);
        despawnRadiusSlider.setMajorTickUnit(2000);
        despawnRadiusSlider.setBlockIncrement(500);
        despawnRadiusSlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(despawnRadiusLabelText, "despawn_radius"), 0, 1);
        grid.add(despawnRadiusSlider, 1, 1);
        grid.add(createValueLabel(despawnRadiusSlider, "%.0f"), 2, 1);

        gameDifficultyFactorSlider = new Slider(0.01, 1.0, Config.GAME_DIFFICULTY_FACTOR);
        gameDifficultyFactorSlider.setShowTickMarks(true);
        gameDifficultyFactorSlider.setShowTickLabels(true);
        gameDifficultyFactorSlider.setMajorTickUnit(0.5);
        gameDifficultyFactorSlider.setBlockIncrement(0.1);
        gameDifficultyFactorSlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(gameDifficultyFactorLabelText, "difficulty_factor"), 0, 2);
        grid.add(gameDifficultyFactorSlider, 1, 2);
        grid.add(createValueLabel(gameDifficultyFactorSlider, "%.1f"), 2, 2);

        spawnDurationSlider = new Slider(0.01, 1.0, Config.SPAWN_DURATION_SECONDS);
        spawnDurationSlider.setShowTickMarks(true);
        spawnDurationSlider.setShowTickLabels(true);
        spawnDurationSlider.setMajorTickUnit(0.2);
        spawnDurationSlider.setBlockIncrement(0.05);
        spawnDurationSlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(spawnDurationLabelText, "spawn_duration"), 0, 3);
        grid.add(spawnDurationSlider, 1, 3);
        grid.add(createValueLabel(spawnDurationSlider, "%.2f"), 2, 3);

        depthDivisorSlider = new Slider(1000, 20000, Config.DEPTH_DIVISOR);
        depthDivisorSlider.setShowTickMarks(true);
        depthDivisorSlider.setShowTickLabels(true);
        depthDivisorSlider.setMajorTickUnit(5000);
        depthDivisorSlider.setBlockIncrement(1000);
        depthDivisorSlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(depthDivisorLabelText, "depth_divisor"), 0, 4);
        grid.add(depthDivisorSlider, 1, 4);
        grid.add(createValueLabel(depthDivisorSlider, "%.0f"), 2, 4);

        maxDepthAlphaSlider = new Slider(0.1, 1.0, Config.MAX_DEPTH_ALPHA);
        maxDepthAlphaSlider.setShowTickMarks(true);
        maxDepthAlphaSlider.setShowTickLabels(true);
        maxDepthAlphaSlider.setMajorTickUnit(0.2);
        maxDepthAlphaSlider.setBlockIncrement(0.05);
        maxDepthAlphaSlider.getStyleClass().add("styled-slider");
        grid.add(createSettingLabel(maxDepthAlphaLabelText, "max_depth_alpha"), 0, 5);
        grid.add(maxDepthAlphaSlider, 1, 5);
        grid.add(createValueLabel(maxDepthAlphaSlider, "%.2f"), 2, 5);

        return grid;
    }

    private GridPane createLanguageTab() {
        GridPane grid = createSettingsGrid();

        languageCombo = new ComboBox<>(FXCollections.observableArrayList(
                "English", "简体中文", "العربية"
        ));

        if (Config.LANGUAGE.equals("en")) {
            languageCombo.setValue("English");
        } else if (Config.LANGUAGE.equals("zh")) {
            languageCombo.setValue("简体中文");
        } else if (Config.LANGUAGE.equals("ar")) {
            languageCombo.setValue("العربية");
        } else {
            languageCombo.setValue("English");
        }

        languageCombo.setMinWidth(250);
        languageCombo.getStyleClass().add("combo-box");
        grid.add(createSettingLabel(languageLabelText, "language_title"), 0, 0);
        grid.add(languageCombo, 1, 0);

        return grid;
    }

    private HBox createBottomBar() {
        HBox bottomBar = new HBox(30);
        bottomBar.setPadding(new Insets(20, 50, 20, 50));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.getStyleClass().add("settings-bottom-bar");

        resetButton = new Button();
        resetButton.getStyleClass().add("settings-button");
        resetButton.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("reset_defaults"),
                Resource.currentLocaleProperty()));
        resetButton.setOnAction(e -> resetToDefaults());

        saveButton = new Button();
        saveButton.getStyleClass().add("settings-button");
        saveButton.textProperty().bind(Bindings.createStringBinding(() -> Resource.getString("save_settings"),
                Resource.currentLocaleProperty()));
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(e -> saveSettings());

        bottomBar.getChildren().addAll(resetButton, saveButton);
        return bottomBar;
    }

    private GridPane createSettingsGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("settings-grid");
        grid.setHgap(30);
        grid.setVgap(20);
        grid.setPadding(new Insets(40));
        return grid;
    }

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

    private Label createVolumeValueLabel(Slider slider) {
        return createValueLabel(slider, "%.0f%%");
    }

    private Label createValueLabel(Slider slider, String format) {
        Label label = new Label();
        label.textProperty().bind(Bindings.createStringBinding(() -> String.format(format, slider.getValue()),
                slider.valueProperty()));
        label.getStyleClass().add("value-label");
        label.setMinWidth(100);
        return label;
    }

    /**
     * Creates a setting label whose text dynamically updates with the current locale.
     *
     * @param labelTextProperty The SimpleStringProperty to bind the label's text to.
     * @param resourceKey       The key to retrieve the localized string from Resource bundle.
     * @return The Label with bound text.
     */
    private Label createSettingLabel(SimpleStringProperty labelTextProperty, String resourceKey) {
        Label label = new Label();
        labelTextProperty.bind(Bindings.createStringBinding(() -> Resource.getString(resourceKey) + ":",
                Resource.currentLocaleProperty()));
        label.textProperty().bind(labelTextProperty);
        label.getStyleClass().add("setting-label");
        label.setMinWidth(250);
        return label;
    }

    /**
     * Saves the current settings from the UI components to the Config.
     * This method updates various game configuration parameters and then applies
     * display settings and saves the configuration to a file.
     */
    private void saveSettings() {
        Config.MASTER_VOLUME = masterVolumeSlider.getValue();
        Config.MUSIC_VOLUME = musicVolumeSlider.getValue();
        Config.SFX_VOLUME = sfxVolumeSlider.getValue();
        Config.RESOLUTION = resolutionCombo.getValue();
        Config.FULLSCREEN = fullscreenToggle.isSelected();
        Config.SHOW_FPS = showFpsToggle.isSelected();

        String selectedDifficultyDisplay = userDifficultyCombo.getValue();
        if (selectedDifficultyDisplay != null) {
            if (selectedDifficultyDisplay.equals(Resource.getString("easy"))) {
                Config.USER_DIFFICULTY = "easy";
            } else if (selectedDifficultyDisplay.equals(Resource.getString("normal"))) {
                Config.USER_DIFFICULTY = "normal";
            } else if (selectedDifficultyDisplay.equals(Resource.getString("hard"))) {
                Config.USER_DIFFICULTY = "hard";
            }
        }

        Config.CAMERA_SENSITIVITY = cameraSensitivitySlider.getValue();

        String selectedLanguageDisplay = languageCombo.getValue();
        if ("English".equals(selectedLanguageDisplay)) {
            Config.LANGUAGE = "en";
        } else if ("简体中文".equals(selectedLanguageDisplay)) {
            Config.LANGUAGE = "zh";
        } else if ("العربية".equals(selectedLanguageDisplay)) {
            Config.LANGUAGE = "ar";
        }

        Config.SPAWN_RADIUS = spawnRadiusSlider.getValue();
        Config.DESPAWN_RADIUS = despawnRadiusSlider.getValue();
        Config.GAME_DIFFICULTY_FACTOR = gameDifficultyFactorSlider.getValue();
        Config.SPAWN_DURATION_SECONDS = spawnDurationSlider.getValue();
        Config.DEPTH_DIVISOR = depthDivisorSlider.getValue();
        Config.MAX_DEPTH_ALPHA = maxDepthAlphaSlider.getValue();

        applyDisplaySettings();
        Config.saveConfig();
        Resource.loadLanguage(Config.LANGUAGE);
    }

    /**
     * Resets all settings to their default values as defined in the Config class.
     * After resetting the values, it updates the UI elements to reflect these changes
     * and reloads the language resources.
     */
    private void resetToDefaults() {
        Config.MASTER_VOLUME = 80.0;
        Config.MUSIC_VOLUME = 70.0;
        Config.SFX_VOLUME = 90.0;
        Config.RESOLUTION = "1920x1080";
        Config.FULLSCREEN = true;
        Config.SHOW_FPS = false;
        Config.LANGUAGE = "en";
        Config.CAMERA_SENSITIVITY = 0.5;

        Config.SPAWN_RADIUS = 2000.0;
        Config.DESPAWN_RADIUS = 4000.0;
        Config.GAME_DIFFICULTY_FACTOR = 0.1;
        Config.SPAWN_DURATION_SECONDS = 0.1;
        Config.DEPTH_DIVISOR = 10000.0;
        Config.MAX_DEPTH_ALPHA = 0.95;

        masterVolumeSlider.setValue(Config.MASTER_VOLUME);
        musicVolumeSlider.setValue(Config.MUSIC_VOLUME);
        sfxVolumeSlider.setValue(Config.SFX_VOLUME);
        resolutionCombo.setValue(Config.RESOLUTION);
        fullscreenToggle.setSelected(Config.FULLSCREEN);
        showFpsToggle.setSelected(Config.SHOW_FPS);
        cameraSensitivitySlider.setValue(Config.CAMERA_SENSITIVITY);

        Resource.loadLanguage(Config.LANGUAGE);
        Config.USER_DIFFICULTY = "normal";
        userDifficultyCombo.setValue(Resource.getString(Config.USER_DIFFICULTY));

        languageCombo.setValue("English");

        spawnRadiusSlider.setValue(Config.SPAWN_RADIUS);
        despawnRadiusSlider.setValue(Config.DESPAWN_RADIUS);
        gameDifficultyFactorSlider.setValue(Config.GAME_DIFFICULTY_FACTOR);
        spawnDurationSlider.setValue(Config.SPAWN_DURATION_SECONDS);
        depthDivisorSlider.setValue(Config.DEPTH_DIVISOR);
        maxDepthAlphaSlider.setValue(Config.MAX_DEPTH_ALPHA);
    }

    /**
     * Applies the selected display settings, such as resolution and fullscreen mode,
     * to the primary stage of the application.
     */
    private void applyDisplaySettings() {
        String[] res = Config.RESOLUTION.split("x");
        if (res.length == 2) {
            try {
                int width = Integer.parseInt(res[0]);
                int height = Integer.parseInt(res[1]);
                if (!Config.FULLSCREEN) {
                    Manager.getPrimaryStage().setWidth(width);
                    Manager.getPrimaryStage().setHeight(height);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid resolution format in config: " + Config.RESOLUTION);
            }
        }

        Manager.getPrimaryStage().setFullScreen(Config.FULLSCREEN);
    }

    /**
     * Updates all localized text elements in the settings screen.
     * This method is called when the locale changes.
     */
    private void updateLocalizedText() {
        if (userDifficultyCombo != null) {
            userDifficultyCombo.setItems(FXCollections.observableArrayList(
                    Resource.getString("easy"),
                    Resource.getString("normal"),
                    Resource.getString("hard")));
            userDifficultyCombo.setValue(Resource.getString(Config.USER_DIFFICULTY));
        }

        if (languageCombo != null) {
            if (Config.LANGUAGE.equals("en")) {
                languageCombo.setValue("English");
            } else if (Config.LANGUAGE.equals("zh")) {
                languageCombo.setValue("简体中文");
            } else if (Config.LANGUAGE.equals("ar")) {
                languageCombo.setValue("العربية");
            } else {
                languageCombo.setValue("English");
            }
        }
    }
}