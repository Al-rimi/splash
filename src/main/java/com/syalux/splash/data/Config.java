package com.syalux.splash.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {
    // Window & Game Mechanics Configuration
    public static double GAME_WIDTH = 1920;
    public static double GAME_HEIGHT = 1080;
    public static double SPAWN_RADIUS = 2000;
    public static double DESPAWN_RADIUS = 4000;
    public static double GAME_DIFFICULTY_FACTOR = 0.3; // Renamed to avoid clash with user preference difficulty
    public static double SPAWN_DURATION_SECONDS = 0.1;

    // Camera Configuration
    public static double DEPTH_DIVISOR = 10000.0;
    public static double MAX_DEPTH_ALPHA = 0.95;
    public static double CAMERA_SENSITIVITY = 1.0; // Moved from Profile

    // User Preferences (Moved from Profile class)
    public static double MASTER_VOLUME = 80.0;
    public static double MUSIC_VOLUME = 70.0;
    public static double SFX_VOLUME = 90.0;
    public static String RESOLUTION = "1920x1080";
    public static boolean FULLSCREEN = true;
    public static boolean SHOW_FPS = false;
    public static String USER_DIFFICULTY = "Normal"; // Renamed to clearly indicate user preference
    public static String LANGUAGE = "en"; // New: User's preferred language

    // Resource Configuration (these are typically fixed and don't change at runtime via settings)
    public static final String DEFAULT_LANGUAGE = "en";
    public static final int FISH_IMAGE_COUNT = 19;
    public static final int MOUNTAIN_IMAGE_COUNT = 3;
    public static final int SEAWEED_IMAGE_COUNT = 2;
    public static final int ROCK_IMAGE_COUNT = 2;
    public static final int BUBBLE_IMAGE_COUNT = 2;

    private static final String CONFIG_FILE = "game_config.properties";

    public static void saveConfig() {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            // Game Mechanics Configuration
            prop.setProperty("GAME_WIDTH", String.valueOf(GAME_WIDTH));
            prop.setProperty("GAME_HEIGHT", String.valueOf(GAME_HEIGHT));
            prop.setProperty("SPAWN_RADIUS", String.valueOf(SPAWN_RADIUS));
            prop.setProperty("DESPAWN_RADIUS", String.valueOf(DESPAWN_RADIUS));
            prop.setProperty("GAME_DIFFICULTY_FACTOR", String.valueOf(GAME_DIFFICULTY_FACTOR));
            prop.setProperty("SPAWN_DURATION_SECONDS", String.valueOf(SPAWN_DURATION_SECONDS));
            prop.setProperty("DEPTH_DIVISOR", String.valueOf(DEPTH_DIVISOR));
            prop.setProperty("MAX_DEPTH_ALPHA", String.valueOf(MAX_DEPTH_ALPHA));
            prop.setProperty("CAMERA_SENSITIVITY", String.valueOf(CAMERA_SENSITIVITY)); // Save Camera Sensitivity

            // User Preferences
            prop.setProperty("MASTER_VOLUME", String.valueOf(MASTER_VOLUME));
            prop.setProperty("MUSIC_VOLUME", String.valueOf(MUSIC_VOLUME));
            prop.setProperty("SFX_VOLUME", String.valueOf(SFX_VOLUME));
            prop.setProperty("RESOLUTION", RESOLUTION);
            prop.setProperty("FULLSCREEN", String.valueOf(FULLSCREEN));
            prop.setProperty("SHOW_FPS", String.valueOf(SHOW_FPS));
            prop.setProperty("USER_DIFFICULTY", USER_DIFFICULTY);
            prop.setProperty("LANGUAGE", LANGUAGE); // New: Save language

            prop.store(output, null);
        } catch (IOException io) {
            System.err.println("Error saving configuration: " + io.getMessage());
            io.printStackTrace();
        }
    }

    public static void loadConfig() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            prop.load(input);

            // Game Mechanics Configuration
            GAME_WIDTH = Double.parseDouble(prop.getProperty("GAME_WIDTH", String.valueOf(GAME_WIDTH)));
            GAME_HEIGHT = Double.parseDouble(prop.getProperty("GAME_HEIGHT", String.valueOf(GAME_HEIGHT)));
            SPAWN_RADIUS = Double.parseDouble(prop.getProperty("SPAWN_RADIUS", String.valueOf(SPAWN_RADIUS)));
            DESPAWN_RADIUS = Double.parseDouble(prop.getProperty("DESPAWN_RADIUS", String.valueOf(DESPAWN_RADIUS)));
            GAME_DIFFICULTY_FACTOR = Double.parseDouble(prop.getProperty("GAME_DIFFICULTY_FACTOR", String.valueOf(GAME_DIFFICULTY_FACTOR)));
            SPAWN_DURATION_SECONDS = Double.parseDouble(prop.getProperty("SPAWN_DURATION_SECONDS", String.valueOf(SPAWN_DURATION_SECONDS)));
            DEPTH_DIVISOR = Double.parseDouble(prop.getProperty("DEPTH_DIVISOR", String.valueOf(DEPTH_DIVISOR)));
            MAX_DEPTH_ALPHA = Double.parseDouble(prop.getProperty("MAX_DEPTH_ALPHA", String.valueOf(MAX_DEPTH_ALPHA)));
            CAMERA_SENSITIVITY = Double.parseDouble(prop.getProperty("CAMERA_SENSITIVITY", String.valueOf(CAMERA_SENSITIVITY))); // Load Camera Sensitivity

            // User Preferences
            MASTER_VOLUME = Double.parseDouble(prop.getProperty("MASTER_VOLUME", String.valueOf(MASTER_VOLUME)));
            MUSIC_VOLUME = Double.parseDouble(prop.getProperty("MUSIC_VOLUME", String.valueOf(MUSIC_VOLUME)));
            SFX_VOLUME = Double.parseDouble(prop.getProperty("SFX_VOLUME", String.valueOf(SFX_VOLUME)));
            RESOLUTION = prop.getProperty("RESOLUTION", RESOLUTION);
            FULLSCREEN = Boolean.parseBoolean(prop.getProperty("FULLSCREEN", String.valueOf(FULLSCREEN)));
            SHOW_FPS = Boolean.parseBoolean(prop.getProperty("SHOW_FPS", String.valueOf(SHOW_FPS)));
            USER_DIFFICULTY = prop.getProperty("USER_DIFFICULTY", USER_DIFFICULTY);
            LANGUAGE = prop.getProperty("LANGUAGE", DEFAULT_LANGUAGE); // New: Load language

        } catch (IOException | NumberFormatException e) {
            System.out.println("No existing configuration file found or invalid format. Using default configuration.");
        }
    }
}