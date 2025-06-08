package com.syalux.splash.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class Resource {
    private static ResourceBundle bundle;
    private static final ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<>(Locale.ENGLISH);
    private static final Map<Integer, Image> fishImages = new HashMap<>();
    private static final Map<Integer, Image> mountainImages = new HashMap<>();
    private static final Map<Integer, Image> rockImages = new HashMap<>();
    private static final Map<Integer, Image> seaweedImages = new HashMap<>();
    private static final Map<Integer, Image> bubbleImages = new HashMap<>();
    private static Image waterTexture;
    private static Image coinImage;
    private static String styles;

    public enum Environment {
        MOUNTAIN,
        ROCK,
        SEAWEED,
        BUBBLE,
        COIN
    }

    public static void loadAll() {
        loadLanguage(Config.LANGUAGE);
        loadStyles();
        loadFishImages();
        loadMountainImages();
        loadRockImages();
        loadSeaweedImages();
        loadWaterTexture();
        loadBubbleImages();
        loadCoinImage();
    }

    public static ObjectProperty<Locale> currentLocaleProperty() {
        return currentLocale;
    }

    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    public static Image getLogo(){
        return loadImage("/images/splash-logo.ico");
    }

    public static Image getFishImage(int fishNumber) {
        if (fishNumber < 1 || fishNumber > Config.FISH_IMAGE_COUNT) {
            System.err.println("Fish number must be between 1 and " + Config.FISH_IMAGE_COUNT + ": " + fishNumber);
            return null;
        }
        return fishImages.get(fishNumber);
    }

    public static Image getEnvironmentImage(Environment type, int imageNumber) {
        if (imageNumber < 1) {
            System.err.println("Image number must be at least 1");
            return null;
        }

        switch (type) {
            case MOUNTAIN:
                return mountainImages.get(imageNumber);
            case ROCK:
                return rockImages.get(imageNumber);
            case SEAWEED:
                return seaweedImages.get(imageNumber);
            case BUBBLE:
                return bubbleImages.get(imageNumber);
            case COIN:
                // For Environment.COIN, we always return the single coinImage
                return coinImage;
            default:
                return null;
        }
    }

    public static Image getWaterTexture() {
        return waterTexture;
    }

    public static Image getCoinImage() {
        return coinImage;
    }

    public static Image loadImage(String path) {
        try (InputStream is = Resource.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Resource not found: " + path);
                return null;
            }
            return new Image(is);
        } catch (IOException e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image content or format for: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public static String getStyleSheet() {
        return styles;
    }

    public static void loadLanguage(String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        try {
            bundle = ResourceBundle.getBundle("lang/messages", locale);
            currentLocale.set(locale);
        } catch (MissingResourceException e) {
            System.err.println("Language file not found: " + lang);
            bundle = ResourceBundle.getBundle("lang/messages", Locale.ENGLISH);
            currentLocale.set(Locale.ENGLISH);
        }
    }

    public static void loadStyles() {
        try {
            styles = Resource.class.getResource("/css/styles.css").toExternalForm();
        } catch (NullPointerException e) {
            System.err.println("Error loading styles.css");
        }
    }

    private static void loadFishImages() {
        for (int i = 1; i <= Config.FISH_IMAGE_COUNT; i++) {
            String path = String.format("/images/characters/fish-%d.png", i);
            Image image = loadImage(path);
            if (image != null) {
                fishImages.put(i, image);
            } else {
                System.err.println("Failed to load fish image: " + i);
            }
        }
    }

    private static void loadMountainImages() {
        for (int i = 1; i <= Config.MOUNTAIN_IMAGE_COUNT; i++) {
            String path = String.format("/images/environment/mountain-%d.png", i);
            Image image = loadImage(path);
            if (image != null) {
                mountainImages.put(i, image);
            } else {
                System.err.println("Failed to load mountain image: " + i);
            }
        }
    }

    private static void loadRockImages() {
        for (int i = 1; i <= Config.ROCK_IMAGE_COUNT; i++) {
            String path = String.format("/images/environment/rock-%d.png", i);
            Image image = loadImage(path);
            if (image != null) {
                rockImages.put(i, image);
            } else {
                System.err.println("Failed to load rock image: " + i);
            }
        }
    }

    private static void loadSeaweedImages() {
        for (int i = 1; i <= Config.SEAWEED_IMAGE_COUNT; i++) {
            String path = String.format("/images/environment/seaweeds-%d.png", i);
            Image image = loadImage(path);
            if (image != null) {
                seaweedImages.put(i, image);
            } else {
                System.err.println("Failed to load seaweed image: " + i);
            }
        }
    }

    private static void loadBubbleImages() {
        for (int i = 1; i <= Config.BUBBLE_IMAGE_COUNT; i++) {
            String path = String.format("/images/environment/bubble-%d.png", i);
            Image image = loadImage(path);
            if (image != null) {
                bubbleImages.put(i, image);
            } else {
                System.err.println("Failed to load bubble image: " + i);
            }
        }
    }

    private static void loadWaterTexture() {
        waterTexture = loadImage("/images/environment/texture-water.png");
        if (waterTexture == null) {
            System.err.println("Failed to load water texture.");
        }
    }

    private static void loadCoinImage() {
        coinImage = loadImage("/images/environment/coin.png");
        if (coinImage == null) {
            System.err.println("Failed to load coin image.");
        }
    }
}
